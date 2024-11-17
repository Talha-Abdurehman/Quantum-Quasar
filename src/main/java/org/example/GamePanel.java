package org.example;

import org.inputs.KeyboardInputs;
import org.inputs.MouseInput;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;


public class GamePanel extends JPanel {

    int HEIGHT = 300;
    int WIDTH = 100;
    private BufferedImage img;

    private float XDelta = 100, YDelta = 100;

    public GamePanel(){
        MouseInput mouseinput = new MouseInput(this);
        System.out.println("GayPanel Working");
        setPanelSize();
        importImg();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseinput);
        addMouseMotionListener(mouseinput);

    }

    private void importImg() {
        InputStream istr = getClass().getResourceAsStream("/Nautolan_Ship_Battlecruiser_Base.png");
        try {
            img = ImageIO.read(istr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setPanelSize() {
        Dimension size = new Dimension(1280,800);
        setPreferredSize(size);
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

        g.drawImage(img,(int)XDelta - 50,(int)YDelta - 50,null);


    }
    
}
