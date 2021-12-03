package com.ladudu.projectx.sprites.Others;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.screens.PlayScreen;

public class FireBall extends Sprite {
    private PlayScreen screen;
    private World world;
    private boolean toDestroy;
    private boolean destroyed;
    private Body b2body;

    public FireBall(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        toDestroy = false;
        destroyed = false;

        defineFireBall();

        setBounds(getX(), getY(), 8 / ProjectX.PPM, 8 /ProjectX.PPM);
        setRegion(screen.getAtlas().findRegion("fireball"), 0, 0,8, 8);
    }

    public void defineFireBall(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(4/ ProjectX.PPM);
        fdef.filter.categoryBits = ProjectX.FIREBALL_BIT;
        fdef.filter.maskBits = ProjectX.OBJECT_BIT |
                ProjectX.GROUND_BIT |
                ProjectX.COIN_BIT |
                ProjectX.BRICK_BIT |
                ProjectX.ENEMY_BIT;

        fdef.shape = shape;
        b2body.setLinearVelocity(2,0);
        b2body.createFixture(fdef).setUserData(this);
    }

    public void update(float dt){
        if (toDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;

        }
        else if (!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }
    }

    public void draw(Batch batch){
        if (!destroyed) super.draw(batch);
    }

    public void destroy(){
        toDestroy = true;
    }
}
