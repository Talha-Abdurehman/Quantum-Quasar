package org.example;

import org.inputs.KeyboardInputs;
import org.inputs.MouseInput;
import static org.constants.Constants.PlayerConstants.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;


public class GamePanel extends JPanel {

    int HEIGHT = 300;
    int WIDTH = 100;
    private BufferedImage img, engineImg,baseEngImg;
    private BufferedImage[][] animations;
    private int animTick, animIndex, animspeed = 13;
    public int playerAction = IDLE;

    private int XDelta = 100, YDelta = 100;

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
        animations = new BufferedImage[2][4];


        for(int i = 0; i < animations.length; i++){

            for(int j = 0; j < animations[i].length;j++){
                animations[i][j] = engineImg.getSubimage(j*48,i*48,48,48);

            }
        }
    }

    private void importImg() {
        InputStream istr = getClass().getResourceAsStream("/Main_Ship_Base_Full_health.png");
        InputStream istr1 = getClass().getResourceAsStream("/Main_Ship_Engines_Supercharged_Engine_Spritesheet.png");
        InputStream istr2 = getClass().getResourceAsStream("/Main_Ship_Engines_Base_Engine.png");
        try {
            img = ImageIO.read(istr);
            engineImg = ImageIO.read(istr1);
            baseEngImg = ImageIO.read(istr2);

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


        g.drawImage(animations[playerAction][animIndex],XDelta,YDelta, 200, 160, null);
        g.drawImage(baseEngImg,XDelta,YDelta,200,160,null);
        g.drawImage(img,XDelta,YDelta, 200, 160, null);




    }

    private void updateAnimationTicker() {
        animTick++;
        if(animTick >= animspeed) {
            animTick = 0;
            animIndex++;

            if(animIndex >= animations.length) {
                animIndex = 0;
            }
        }

    }

}
