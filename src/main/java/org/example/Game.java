package org.example;

public class Game implements Runnable {
    private final int FPS_SET = 120;
    private Thread thread;
    private final GamePanel gamePanel;
    private final GameWindow gameWindow;


    public Game() {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }


    private void startGameLoop() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();
        int frames = 0;
        long lastCheck = System.currentTimeMillis();


        while (true) {

            now = System.nanoTime();
            if (now - lastFrame >= timePerFrame) {
                gamePanel.repaint();
                lastFrame = now;
                frames++;

            }


            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }


        }

    }
}
