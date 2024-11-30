package org.example;

import org.entities.Background;
import org.entities.Player;
import org.json.JSONObject;
import org.multiplayer.GameClient;

import java.awt.*;

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
    private Background background;
    private GameClient client;

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
            player = new Player(200, 200, 160, 300, true);
            System.out.println("X coordinates " + player.getX());
            System.out.println("Trying to connect");
            client = new GameClient("http://localhost:3000");
            JSONObject initialData = new JSONObject();
            initialData.put("x", player.getX());
            initialData.put("y", player.getY());
            client.joinGame(initialData);

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

        try {
            JSONObject playerData = new JSONObject();
            playerData.put("x", player.getX());
            playerData.put("y", player.getY());
            client.sendPlayerData(playerData);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void render(Graphics g) {
        background.renderBackground(g);
        player.render(g);
        player.drawBullets(g);
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

}
