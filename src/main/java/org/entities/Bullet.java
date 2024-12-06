package org.entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


public class Bullet {
    float x, y;
    int speed;
    private int animTick;
    private int moveIndex;
    private final int animspeed = 13;
    private BufferedImage[] bulletAnim;
    private BufferedImage bulletImg;

    public Bullet(float x, float y) {
        this.x = x;
        this.y = y;
        this.speed = 2;
        loadBulletAnimation();
    }


    public void update() {
        y -= speed;
        updateAnimation();

    }

    public void updateAnimation() {
        animTick++;
        if (animTick >= animspeed) {
            animTick = 0;
            moveIndex++;

            if (moveIndex >= bulletAnim.length) {
                moveIndex = 0;
            }
        }
    }

    public void drawBullet(Graphics g) {
        g.drawImage(bulletAnim[moveIndex], (int) x + 12, (int) y - 6, 100, 80, null);
        g.drawImage(bulletAnim[moveIndex], (int) x + 88, (int) y - 6, 100, 80, null);
    }

    public void loadBulletAnimation() {

        InputStream istr4 = getClass().getResourceAsStream("/Main_ship_weapon_Projectile_Auto_cannon_bullet.png");

        try {
            bulletImg = ImageIO.read(istr4);
            bulletAnim = new BufferedImage[4];

            for (int i = 0; i < bulletAnim.length; i++) {
                bulletAnim[i] = bulletImg.getSubimage(i * 32, 0, 32, 32);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                istr4.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public boolean isOffscreen() {
        if (this.y < -300) {
            return true;
        } else {
            return false;
        }
    }
}

