package org.inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {

            case KeyEvent.VK_W:
                System.out.println("W Pressed");
                break;
            case KeyEvent.VK_S:
                System.out.println("S Pressed");
                break;
            case KeyEvent.VK_A:
                System.out.println("A Pressed");
                break;
            case KeyEvent.VK_D:
                System.out.println("D Pressed");
                break;
            case KeyEvent.VK_SPACE:
                System.out.println("Space Bar Pressed");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {




    }
}
