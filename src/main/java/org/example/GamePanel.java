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
    private BufferedImage img, engineImg;
    private BufferedImage[] idleAnim,engineAnim;
    private int animTick, animIndex, animspeed = 13;

    private float XDelta = 100, YDelta = 100;

    public GamePanel() {
        MouseInput mouseinput = new MouseInput(this);
        System.out.println("GayPanel Working");
        setPanelSize();
        importImg();
        importAnim();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseinput);
        addMouseMotionListener(mouseinput);

    }

    private void importAnim() {
        idleAnim = new BufferedImage[9];
        engineAnim = new BufferedImage[8];

        for(int i = 0; i < idleAnim.length; i++){
            idleAnim[i] = img.getSubimage(i*128,0,128,128);
        }
        for(int j = 0; j < engineAnim.length; j++){
            engineAnim[j] = engineImg.getSubimage(j*128,0,128,128);
        }
    }

    private void importImg() {
        InputStream istr = getClass().getResourceAsStream("/Nautolan_Ship_Battlecruiser_Weapons.png");
        InputStream istr1 = getClass().getResourceAsStream("/Nautolan_Ship_Battlecruiser_Engine_Effect.png");
        try {
            img = ImageIO.read(istr);
            engineImg = ImageIO.read(istr1);

            if (img == null || engineImg == null) {
                throw new Exception("One or more images could not be loaded.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    //===================================================
    public void changeXDelta(int value) {

        this.XDelta += value;
        repaint();
    }


    public void changeYDelta(int value) {

        this.YDelta += value;
        repaint();
    }
    //==================================================


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateAnimationTicker();

        g.drawImage(idleAnim[animIndex], 100, 100, null);
        g.drawImage(engineAnim[animIndex], 100, 100, null);

    }

    private void updateAnimationTicker() {
        animTick++;
        if(animTick >= animspeed) {
            animTick = 0;
            animIndex++;

            if(animIndex >= idleAnim.length) {
                animIndex = 0;
            }
        }

    }

}
