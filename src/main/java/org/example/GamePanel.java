package org.example;

import org.inputs.KeyboardInputs;
import org.inputs.MouseInput;
import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {

    private int frames = 0;
    private long lastCheck = 0;
    int HEIGHT = 300;
    int WIDTH = 100;

    private float XDelta = 100, YDelta = 100;
    private float XDir = 1f, YDir = 1f;
    public GamePanel(){
        MouseInput mouseinput = new MouseInput(this);
        System.out.println("GayPanel Working");
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseinput);
        addMouseMotionListener(mouseinput);

    }

    //===================================================
    public void changeXDelta(int value){

        this.XDelta += value;
        repaint();
    }


    public void changeYDelta(int value){

        this.YDelta += value;
        repaint();
    }
    //==================================================


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        updateRect();
        g.setColor(Color.red);
        g.fillRect((int)XDelta,(int)YDelta,WIDTH,HEIGHT);

    }

    public void updateRect() {
        XDelta += XDir;

        if(XDelta > 800 || XDelta < 0){
            XDir*=-1;
        }

        YDelta += YDir;
        if(YDelta > 600 || YDelta < 0) {
            YDir *= -1;
        }

    }


    public void setRect(int x, int y){
        this.XDelta = x;
        this.YDelta = y;
        repaint();
    }
}
