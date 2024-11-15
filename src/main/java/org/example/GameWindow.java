package org.example;

import javax.swing.*;

public class GameWindow {

    public GameWindow(GamePanel gamePanel){
        System.out.println("Gayme has begun");
        JFrame frame = new JFrame();
        frame.setSize(800,600);
        frame.add(gamePanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("Top Down Shooter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

}
