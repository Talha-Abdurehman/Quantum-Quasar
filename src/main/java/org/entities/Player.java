package org.entities;

import javax.imageio.ImageIO;
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
    public int playerHealth = FULL_HEALTH;
    private BufferedImage img, engineImg, baseEngImg, cannonImg, bulletImg;
    private BufferedImage[][] animations;
    private BufferedImage[] attackAnim, bulletAnim;
    private int animTick;
    private int moveIndex;
    private final int animspeed = 13;
    private int atkspeed, atkIndex, atkTick = 5;
    private boolean moving, attacking = false;
    private int playerDir = -1;
    private boolean left, up, right, down;
    private final boolean isLocal;
    private final String id;
    private static final float MAX_LERP_SPEED = 0.3f;
    private static final float MIN_LERP_SPEED = 0.05f;
    private static final long MAX_INTERPOLATION_DELAY = 300;
    private float targetX, targetY;
    private float predictedX, predictedY;
    private float velocityX = 0;
    private float velocityY = 0;
    private float lastX;
    private float lastY;
    private long lastUpdateTime;
    private final long lastUpdateTimestamp = 0;
    private static final long MAX_INTERPOLATION_TIME = 200;
    private long lastValidUpdateTimestamp = 0;
    private int hits = 0;

    private final float playerSpeed = 1.5f;

    public Player(String id, float x, float y, int width, int height, boolean isLocal) {
        super(x, y, width, height);
        this.id = id;
        this.playerHealth = getHealthSprite(hits);
        this.targetX = x;
        this.targetY = y;
        this.isLocal = isLocal;
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

    public void updatePos() {
        long currentTime = System.currentTimeMillis();
        // Calculate time since the last update
        if (lastUpdateTime != 0) {
            long deltaTime = currentTime - lastUpdateTime; // In milliseconds
            if (deltaTime > 0) {
                velocityX = (x - lastX) / (deltaTime / 1000f); // Convert to seconds
                velocityY = (y - lastY) / (deltaTime / 1000f); // Convert to seconds
            }
        }
        // Update last known position and time
        lastX = x;
        lastY = y;
        lastUpdateTime = currentTime;
        // Perform movement logic
        if (isLocal) {
            if (left && !right) x -= playerSpeed;
            if (right && !left) x += playerSpeed;
            if (up && !down) y -= playerSpeed;
            if (down && !up) y += playerSpeed;

            // Clamp position to bounds
            x = Math.max(0, Math.min(x, 1080));
            y = Math.max(0, Math.min(y, 550));
        }
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

    public void updateHealth() {
        getHits();
        setPlayerHealth();

    }

    public void drawBullets(Graphics g) {
        for (Bullet bullet : bullets) {
            bullet.drawBullet(g);
        }
    }

    public String getId() {
        return this.id;
    }

    public void reconcileWithServer(float serverX, float serverY, long serverTimestamp) {
        if (serverTimestamp > this.lastValidUpdateTimestamp) {
            float discrepancy = calculateDistance(x, y, serverX, serverY);

            if (discrepancy > 50f) { // Allow some tolerance
                // Correct position immediately for large discrepancies
                x = serverX;
                y = serverY;
            } else {
                // Smoothly adjust for minor discrepancies
                targetX = serverX;
                targetY = serverY;
                interpolatePos();
            }

            this.lastValidUpdateTimestamp = serverTimestamp;
        }
    }


    private float calculateAdaptiveLerpSpeed(long timeSinceLastUpdate) {
        float baseSpeed = 0.1f; // Increase base speed for faster corrections
        float latencyFactor = Math.min(timeSinceLastUpdate / 100f, 1.0f);
        return baseSpeed + latencyFactor * (MAX_LERP_SPEED - baseSpeed);
    }


    private float calculateDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }


    public void interpolatePos() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastUpdate = currentTime - lastUpdateTimestamp;

        // More dynamic interpolation
        float lerpSpeed = calculateAdaptiveLerpSpeed(timeSinceLastUpdate);

        synchronized (this) {
            // Smoother position adjustment
            x += lerpSpeed * (targetX - x);
            y += lerpSpeed * (targetY - y);

            // Precise snapping with more tolerance
            if (Math.abs(targetX - x) < 1f || timeSinceLastUpdate > MAX_INTERPOLATION_DELAY) {
                x = targetX;
                y = targetY;
            }
        }
    }

    private float calculateDynamicLerpSpeed(long timeSinceLastUpdate) {
        // Faster interpolation if updates are delayed
        float baseSpeed = 0.1f;
        float speedMultiplier = Math.min(timeSinceLastUpdate / 100f, 1.0f);
        return baseSpeed * speedMultiplier;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
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

    public int getPlayerHealth() {
        return this.playerHealth;
    }

    public void setPlayerHealth() {
        this.playerHealth = getHealthSprite(hits);
    }

    public void setHits() {
        this.hits += 1;
    }

    public int getHits() {
        if (hits > 5) {
            hits = 0;
        }
        return hits;
    }

    public void createBullet() {
        bullets.add(new Bullet((int) x, (int) y));
    }

}


