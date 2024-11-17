package org.example;

import javax.swing.*;

public class GameWindow {

    public GameWindow(GamePanel gamePanel) {
        System.out.println("Gayme has begun");
        JFrame frame = new JFrame();
        frame.add(gamePanel);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setTitle("Top Down Shooter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

}
