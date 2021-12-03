package com.ladudu.projectx.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ladudu.projectx.screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(0.5f,-1);
        b2body.setActive(false);
    }

    public abstract void update(float dt);

    protected abstract void defineEnemy();
    public abstract void hitOnHead();
    public abstract void eatToxic();

    public void reverseVelocity(boolean x, boolean y) {
        if (x) velocity.x = -velocity.x;
        if (y) velocity.y = -velocity.y;
    }
}
