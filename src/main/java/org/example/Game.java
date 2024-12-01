package org.example;

import org.entities.Background;
import org.entities.Player;
import org.json.JSONObject;
import org.multiplayer.GameClient;
import org.multiplayer.NetworkManager;

import java.awt.*;
import java.util.UUID;

public class Game implements Runnable {
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

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        try {
            background = new Background(GAME_WIDTH, GAME_HEIGHT);
            player = new Player(UUIDGen(), 200, 200, 160, 300, true);
            System.out.println(player.getId());

            // Initialize NetworkManager
            networkManager = new NetworkManager("http://localhost:3000");

            // Set up network listener
            networkManager.setNetworkListener(playerData -> {
                // Handle incoming player updates
                try {
                    String playerId = playerData.getString("id");

                    if (!playerId.equals(player.getId())) {
                        System.out.println("Another Player has joined");
                        System.out.println("Other Player ID " + playerId);
                        System.out.println("Player ID " + player.getId());
                        double x = playerData.getDouble("x");
                        double y = playerData.getDouble("y");
                        // Update other players or handle network player data
                        if (!isOtherPlayerJoined) {
                            otherPlayer = new Player(playerId, (int) x, (int) y, 160, 300, false);
                            isOtherPlayerJoined = true;
                        } else {
                            otherPlayer.setTargetPos((float) x, (float) y);
                        }
                        System.out.println("Received player data: x=" + x + ", y=" + y);
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
        if (currentTime - lastNetworkUpdate >= 80) { // Every 50 ms
            try {
                JSONObject playerData = new JSONObject();
                playerData.put("x", player.getX());
                playerData.put("y", player.getY());
                playerData.put("id", player.getId());
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

}
