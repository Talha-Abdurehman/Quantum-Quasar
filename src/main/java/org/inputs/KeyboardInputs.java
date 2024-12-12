package org.inputs;

import org.example.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
    private final GamePanel gamePanel;
    boolean spacePressed = false;
    boolean escapePressed = false;
    boolean isReloading = false;


    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                gamePanel.getGame().getPlayer().setUp(false);
                gamePanel.getGame().getPlayer().setMoving(false);
                break;
            case KeyEvent.VK_A:
                gamePanel.getGame().getPlayer().setLeft(false);
                gamePanel.getGame().getPlayer().setMoving(false);
                break;
            case KeyEvent.VK_S:
                gamePanel.getGame().getPlayer().setDown(false);
                gamePanel.getGame().getPlayer().setMoving(false);
                break;
            case KeyEvent.VK_D:
                gamePanel.getGame().getPlayer().setRight(false);
                gamePanel.getGame().getPlayer().setMoving(false);
                break;
            case KeyEvent.VK_SPACE:
                spacePressed = false;
                gamePanel.getGame().getPlayer().setAttacking(false);// Reset flag when space is released
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                gamePanel.getGame().getPlayer().setUp(true);
                gamePanel.getGame().getPlayer().setMoving(true);
                break;
            case KeyEvent.VK_A:
                gamePanel.getGame().getPlayer().setLeft(true);
                gamePanel.getGame().getPlayer().setMoving(true);
                break;
            case KeyEvent.VK_S:
                gamePanel.getGame().getPlayer().setDown(true);
                gamePanel.getGame().getPlayer().setMoving(true);
                break;
            case KeyEvent.VK_D:
                gamePanel.getGame().getPlayer().setRight(true);
                gamePanel.getGame().getPlayer().setMoving(true);
                break;

            case KeyEvent.VK_SPACE:
                if (!spacePressed && !isReloading) {
                    spacePressed = true;
                    if (gamePanel.getGame().getPlayer().getNumHits() < 5) {
                        gamePanel.getGame().getPlayer().createBullet();
                        gamePanel.getGame().getPlayer().setAttacking(true);
                        gamePanel.getGame().getAudioManager().fireSFX("Canon_Fire");
                        gamePanel.getGame().getPlayer().setHits();

                        new Thread(() -> {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            gamePanel.getGame().getPlayer().setAttacking(false); // Reset attacking flag
                        }).start();
                    } else {
                        isReloading = true;
                        spacePressed = true;
                        new Thread(() -> {
                            try {
                                Thread.sleep(1500);
                                gamePanel.getGame().getPlayer().resetHits();

                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            } finally {
                                spacePressed = false;
                                isReloading = false;
                            }
                        }).start();
                    }
                }
                break;
            case KeyEvent.VK_ESCAPE:
                if (!escapePressed) {
                    gamePanel.getGame().setGameState(true);
                    escapePressed = true;
                } else {
                    gamePanel.getGame().setGameState(false);
                    escapePressed = false;
                }
                break;

        }
    }

}
