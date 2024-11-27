package org.example;

import org.entities.Background;
import org.entities.Bullet;
import org.entities.Player;

import java.awt.*;

public class Game implements Runnable {
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private Thread thread;
    private final GamePanel gamePanel;
    private final GameWindow gameWindow;
    private Player player;
    private Background background;

    public final static int TILES_DEFAULT_SIZE = 48;
    public final static float SCALE = 1f;
    public final static int TILE_HEIGHT = 14;
    public final static int TILE_WIDTH = 29;
    public final static int TILE_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_HEIGHT = TILE_SIZE * TILE_HEIGHT;
    public final static int GAME_WIDTH = TILE_SIZE * TILE_WIDTH;

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();


    }

    private void initClasses() {
        background = new Background(GAME_WIDTH, GAME_HEIGHT);
        player = new Player(200, 200, 160, 300);


    }


    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        background.update();
        player.update();
        player.updateBullet();

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
