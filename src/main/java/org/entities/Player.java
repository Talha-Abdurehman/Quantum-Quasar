package org.entities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import static org.constants.Constants.PlayerConstants.*;

public class Player extends Entity {

    ArrayList<Bullet> bullets = new ArrayList<>();
    public int playerAction = IDLE;
    public int playerAttack = ATTACK_0;
    private BufferedImage img, engineImg, baseEngImg, cannonImg, bulletImg;
    private BufferedImage[][] animations;
    private BufferedImage[] attackAnim, bulletAnim;
    private int animTick, moveIndex, animspeed = 13;
    private int atkspeed, atkIndex, atkTick = 5;
    private boolean moving, attacking = false;
    private int playerDir = -1;
    private boolean left, up, right, down;

    private float playerSpeed = 1.5f;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        System.out.println();
    }

    // Update method for updating the player
    public void update() {
        updatePos();
        updateAnimationTicker();
        setAnimations();
    }

    private void setAnimations() {
        int startAnim = playerAction;
        int startAtkAnim = playerAttack;

        if (moving)
            playerAction = ACCELERATE;
        else
            playerAction = IDLE;

        if (attacking) {
            playerAttack = ATTACK_1;
        } else
            playerAttack = ATTACK_0;

        if (startAnim != playerAction)
            resetAnimTick();

        if (startAtkAnim != playerAttack)
            resetAnimTick();
    }

    private void resetAnimTick() {
        animTick = 0;
        atkTick = 0;
        moveIndex = 0;
        atkIndex = 0;
    }


    public void render(Graphics g) {


        g.drawImage(animations[playerAction][moveIndex], (int) x, (int) y, 200, 160, null);
        g.drawImage(baseEngImg, (int) x, (int) y, 200, 160, null);
        g.drawImage(img, (int) x, (int) y, 200, 160, null);
        g.drawImage(attackAnim[atkIndex], (int) x, (int) y, 200, 160, null);

    }


    private void loadAnimations() {
        InputStream istr = getClass().getResourceAsStream("/Main_Ship_Base_Full_health.png");
        InputStream istr1 = getClass().getResourceAsStream("/Main_Ship_Engines_Base Engine_Spritesheet.png");
        InputStream istr2 = getClass().getResourceAsStream("/Main_Ship_Engines_Base_Engine.png");
        InputStream istr3 = getClass().getResourceAsStream("/Main_Ship_Weapons_Auto_Cannon.png");
        try {
            img = ImageIO.read(istr);
            engineImg = ImageIO.read(istr1);
            baseEngImg = ImageIO.read(istr2);
            cannonImg = ImageIO.read(istr3);


            animations = new BufferedImage[2][4];
            attackAnim = new BufferedImage[7];


            // MOVEMENT ANIMATION ===============================================================
            for (int i = 0; i < animations.length; i++) {
                for (int j = 0; j < animations[i].length; j++) {
                    animations[i][j] = engineImg.getSubimage(j * 48, i * 48, 48, 48);
                }
            }

            // ATTACK & BULLET ANIMATION ===============================================================
            for (int i = 0; i < attackAnim.length; i++) {
                attackAnim[i] = cannonImg.getSubimage(i * 48, 0, 48, 48);
            }

            if (img == null || engineImg == null) {
                throw new Exception("One or more images could not be loaded.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                istr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void updatePos() {
        moving = false;

        if (left && !right) {
            x -= playerSpeed;
            moving = true;
        } else if (right && !left) {
            x += playerSpeed;
            moving = true;
        }

        if (up && !down) {
            y -= playerSpeed;
            moving = true;
        } else if (down && !up) {
            y += playerSpeed;
            moving = true;
        }

        if (x < 0) x = 0; // Prevent moving out of bounds (left)
        if (y < 0) y = 0; // Prevent moving out of bounds (top)
        if (x > 1080) x = 1080; // Assuming 800 is screen width
        if (y > 550) y = 550; // Assuming 600 is screen height
    }

    public void setDirection(int direction) {
        this.playerDir = direction;
        moving = true;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void updateAnimationTicker() {
        animTick++;
        if (animTick >= animspeed) {
            animTick = 0;
            moveIndex++;

            if (moveIndex >= getSpriteAmount(playerAction)) {
                moveIndex = 0;
            }
        }
        atkTick++; // Other Animations

        if (atkTick >= atkspeed) {
            atkTick = 0;
            atkIndex++;

            if (atkIndex >= getAction(playerAttack)) {
                atkIndex = 0;
            }
        } // Attack Animation
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void createBullet() {
        bullets.add(new Bullet((int) x, (int) y));
    }

    public void updateBullet() {
        Iterator<Bullet> iterator = bullets.iterator();

        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.update();
            if (bullet.isOffscreen()) {
                iterator.remove();
            }
        }

    }

    public void drawBullets(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.drawBullet(g);
        }
    }
}
