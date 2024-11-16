package org.example;

import org.inputs.KeyboardInputs;
import org.inputs.MouseInput;
import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {

    int HEIGHT = 300;
    int WIDTH = 100;

    private int XDelta = 0, YDelta = 0;
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
        g.fillRect(XDelta - WIDTH/2,YDelta - HEIGHT/2,WIDTH,HEIGHT);

        }



    public void setRect(int x, int y){
        this.XDelta = x;
        this.YDelta = y;
        repaint();
    }
}
