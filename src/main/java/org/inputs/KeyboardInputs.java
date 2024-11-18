package org.inputs;

import org.example.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
    private final GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {

            case KeyEvent.VK_W:
                System.out.println("W Pressed");
                gamePanel.changeYDelta(-5);
                gamePanel.playerAction = 1;
                break;
            case KeyEvent.VK_S:
                System.out.println("S Pressed");
                gamePanel.changeYDelta(5);
                gamePanel.playerAction = 0;
                break;
            case KeyEvent.VK_A:
                System.out.println("A Pressed");
                gamePanel.changeXDelta(-5);
                break;
            case KeyEvent.VK_D:
                System.out.println("D Pressed");
                gamePanel.changeXDelta(5);
                break;
            case KeyEvent.VK_SPACE:
                System.out.println("Space Bar Pressed");
                break;
            default:
                System.out.println("NO Key being Pressed");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()){
            case KeyEvent.VK_W:
                gamePanel.playerAction = 0;
        }


    }
}
