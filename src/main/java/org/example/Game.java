package org.example;

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

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();


    }

    private void initClasses() {
        player = new Player(200, 200);

    }


    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    public void update() {
        player.update();
        player.updateBullet();

    }

    public void render(Graphics g) {
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
