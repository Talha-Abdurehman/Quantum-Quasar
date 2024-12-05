package org.inputs;

import org.example.GamePanel;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static org.constants.Constants.PlayerConstants.*;

public class KeyboardInputs implements KeyListener {
    private final GamePanel gamePanel;


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
                gamePanel.getGame().getPlayer().setAttacking(false);
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
                gamePanel.getGame().getPlayer().setAttacking(true);
                gamePanel.getGame().getPlayer().createBullet();
                gamePanel.getGame().getAudioLoader().playSound();
                break;
            case KeyEvent.VK_ESCAPE:
                gamePanel.getGame().setGameState(true);
                break;
            case KeyEvent.VK_P:
                gamePanel.getGame().setGameState(false);
                System.out.println("P was pressed");
                break;

        }
    }
}
