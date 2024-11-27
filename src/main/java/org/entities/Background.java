package org.entities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static org.example.Game.GAME_HEIGHT;
import static org.example.Game.GAME_WIDTH;

public class Background {
    private final int gameWidth, gameHeight;
    public BufferedImage background;
    public BufferedImage[] backgroundAnim, scaledBackgroundAnim;
    public int animTick, animIndex, animSpeed = 13;
    public int randomSelector;
    public String path;

    public Background(int gamewidth, int gameheight) {
        this.gameWidth = gamewidth;
        this.gameHeight = gameheight;
        loadBackground();
    }

    public void loadBackground() {
        randomSelector = randomizer();
        path = String.format("/space%d_4-frames.png", randomSelector);
        InputStream backgroundIstr = getClass().getResourceAsStream(path);
        backgroundAnim = new BufferedImage[4];
        scaledBackgroundAnim = new BufferedImage[4];


        try {
            background = ImageIO.read(backgroundIstr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // LOOPING THROUGH THE IMAGES
        for (int i = 0; i < backgroundAnim.length; i++) {
            backgroundAnim[i] = background.getSubimage(i * 64, 0, 64, 64);
            scaledBackgroundAnim[i] = scaleImage(backgroundAnim[i], gameWidth, gameHeight);
        }
    }

    private BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();

        // Use high-quality scaling
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Draw the original image scaled to fill the entire game window
        g2d.drawImage(originalImage, 0, 0, width, height, null);

        g2d.dispose();
        return scaledImage;
    }


    public void renderBackground(Graphics g) {
        g.drawImage(scaledBackgroundAnim[animIndex], 0, 0, null);
    }

    public void updateAnimation() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;

            if (animIndex >= scaledBackgroundAnim.length) {
                animIndex = 0;
            }
        }

    }

    public int randomizer() {
        Random randomizer = new Random();
        return randomizer.nextInt(1, 9);
    }

    public void update() {
        updateAnimation();
    }


}

