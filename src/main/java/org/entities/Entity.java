package org.entities;

public abstract class Entity {
    protected float x, y = 0;

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }


    public float getY() {
        return y;
    }
}

