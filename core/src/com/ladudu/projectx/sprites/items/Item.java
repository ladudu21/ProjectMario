package com.ladudu.projectx.sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.screens.PlayScreen;
import com.ladudu.projectx.sprites.Yasuo;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body b2body;

    public Item(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);

        toDestroy = false;
        destroyed = false;
        defineItem();
    }

    public abstract void defineItem();
    public abstract void use(Yasuo yasuo);

    public void update(float dt){
        if (toDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        if (!destroyed) super.draw(batch);
    }

    public void destroy(){
        toDestroy = true;
    }

    public void reverseVelocity(boolean x, boolean y) {
        if (x) velocity.x = -velocity.x;
        if (y) velocity.y = -velocity.y;
    }
}
