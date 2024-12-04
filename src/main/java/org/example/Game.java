package org.example;

import org.entities.Background;
import org.entities.Player;
import org.json.JSONObject;
import org.multiplayer.GameClient;
import org.multiplayer.NetworkManager;

import java.awt.*;
import java.util.UUID;

public class Game implements Runnable {
    public boolean IS_PAUSED = false;
    public final static int TILES_DEFAULT_SIZE = 48;
    public final static float SCALE = 1f;
    public final static int TILE_HEIGHT = 14;
    public final static int TILE_WIDTH = 29;
    public final static int TILE_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_HEIGHT = TILE_SIZE * TILE_HEIGHT;
    public final static int GAME_WIDTH = TILE_SIZE * TILE_WIDTH;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private final GamePanel gamePanel;
    private final GameWindow gameWindow;
    private Thread thread;
    private Player player;
    private Player otherPlayer;
    private boolean isOtherPlayerJoined = false;
    private Background background;
    private GameClient client;
    private NetworkManager networkManager;
    private long lastNetworkUpdate;
    private Menu menu;

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        try {
            menu = new Menu();
            background = new Background(GAME_WIDTH, GAME_HEIGHT);
            player = new Player(UUIDGen(), 200, 200, 160, 300, true);
            System.out.println(player.getId());

            // Initialize NetworkManager
            networkManager = new NetworkManager("http://140.238.160.136:3000/");

            // Set up network listener
            networkManager.setNetworkListener(playerData -> {
                try {
                    String playerId = playerData.getString("id");

                    if (!playerId.equals(player.getId())) {
                        System.out.println("Another Player has joined");
                        System.out.println("Other Player ID " + playerId);
                        System.out.println("Player ID " + player.getId());
                        double x = playerData.getDouble("x");
                        double y = playerData.getDouble("y");
                        double velocityX = playerData.getDouble("velocityX"); // Add velocity to playerData on the server
                        double velocityY = playerData.getDouble("velocityY"); // Add velocity to playerData on the server
                        long networkTimestamp = playerData.getLong("serverTimestamp");

                        // Calculate network latency
                        long currentTime = System.currentTimeMillis();
                        long latency = currentTime - networkTimestamp;

                        System.out.printf("Network Update: ID=%s, Position(%.2f, %.2f), Latency=%dms\n",
                                playerId, x, y, latency);

                        // Compensate for latency using velocity
                        double compensatedX = x + (velocityX * latency / 1000.0); // Rewind position based on latency
                        double compensatedY = y + (velocityY * latency / 1000.0);

                        if (!isOtherPlayerJoined) {
                            otherPlayer = new Player(playerId, (int) compensatedX, (int) compensatedY, 160, 300, false);
                            isOtherPlayerJoined = true;
                        } else {
                            // Use reconciled position with compensation
                            otherPlayer.reconcileWithServer((float) compensatedX, (float) compensatedY, networkTimestamp);
                        }

                        System.out.printf("Compensated Position: (%.2f, %.2f)\n", compensatedX, compensatedY);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Start network thread
            Thread networkThread = new Thread(networkManager);
            networkThread.start();

            // Join the game with initial player data
            JSONObject initialData = new JSONObject();
            initialData.put("x", player.getX());
            initialData.put("y", player.getY());
            initialData.put("id", player.getId());
            initialData.put("velocityX", player.getVelocityX()); // Send initial velocity
            initialData.put("velocityY", player.getVelocityY()); // Send initial velocity
            networkManager.joinGame(initialData);

            System.out.println("Sending player data: x=" + player.getX() + ", y=" + player.getY());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        background.update();
        player.update();
        player.updateBullet();

        if (otherPlayer != null) {
            otherPlayer.interpolatePos();
            otherPlayer.update();
            otherPlayer.updateBullet();
        }

        // Send player data at a lower frequency
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastNetworkUpdate >= 16) { // Every 50 ms
            try {
                JSONObject playerData = new JSONObject();
                playerData.put("x", player.getX());
                playerData.put("y", player.getY());
                playerData.put("id", player.getId());
                playerData.put("timestamp", currentTime);
                networkManager.sendPlayerData(playerData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            lastNetworkUpdate = currentTime;
        }
    }


    // Don't forget to add a method to clean up resources
    public void shutdown() {
        networkManager.stop();
    }

    public void render(Graphics g) {
        background.renderBackground(g);
        player.render(g);
        player.drawBullets(g);
        if (otherPlayer != null) {
            otherPlayer.render(g);
        }
        if (IS_PAUSED) {
            g.drawImage(menu.getMenuImage()[3], (int) (GAME_WIDTH / 2.5), GAME_HEIGHT / 8, 30, 20, null);
            g.drawImage(menu.getMenuImage()[1], (int) (GAME_WIDTH / 2.5), GAME_HEIGHT / 8, 300, 500, null);

        }
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previous = System.nanoTime();
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            if (!IS_PAUSED) {

                long currentTime = System.nanoTime();

                deltaU += (currentTime - previous) / timePerUpdate;
                deltaF += (currentTime - previous) / timePerFrame;
                previous = currentTime;


                if (deltaU >= 1) {
                    update();
                    updates++;
                    deltaU--;
                } // DeltaU Method==================================


                if (deltaF >= 1) {
                    gamePanel.repaint();
                    frames++;
                    deltaF--;
                }
            } else {
                gamePanel.repaint();
            }
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }

        }
    }

    public Player getPlayer() {
        return player;
    }

    public String UUIDGen() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public void setGameState(boolean state) {
        this.IS_PAUSED = state;
    }

    public int getGameState() {
        if (IS_PAUSED) {
            return 1;
        } else {
            return 0;
        }
    }

}
