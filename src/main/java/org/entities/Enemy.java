package org.entities;

public abstract class Enemy extends Entity {
    private int anim_idx;
    private int enemy_state;
    private int type;
    private int animTick;
    private final int animSpeed = 25;
    private final int enemyType;


    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
    }

    public void updateAnimationTick() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            anim_idx++;
            if (anim_idx >= 9999) {
                anim_idx = 0;
            }
        }
    }

    public void update() {
        updateAnimationTick();
    }

    public int getAnim_idx() {
        return anim_idx;
    }

    public int getEnemy_state() {
        return enemy_state;
    }


}
