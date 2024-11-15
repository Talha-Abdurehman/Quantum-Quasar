package org.example;

import org.inputs.KeyboardInputs;
import org.inputs.MouseInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel {
    private MouseInput mouseinput;

    public GamePanel(){
        mouseinput = new MouseInput();
        System.out.println("GayPanel Working");
        addKeyListener(new KeyboardInputs());
        addMouseListener(mouseinput);
        addMouseMotionListener(mouseinput);

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.fillRect(100,100,100,100);

        }
    }
