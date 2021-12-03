package com.ladudu.projectx.sprites.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.screens.PlayScreen;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDesTroyed;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), 0, -5, 20, 22));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), 22, -5, 20, 22));
        walkAnimation = new Animation<TextureRegion>(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 20 /ProjectX.PPM, 20 /ProjectX.PPM);

        setToDesTroyed = false;
        destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;

        if(setToDesTroyed && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 42, -6, 20, 22));
            stateTime = 0;
        }
        else if (!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(8/ ProjectX.PPM);

        fdef.filter.categoryBits = ProjectX.ENEMY_BIT;
        fdef.filter.maskBits = ProjectX.GROUND_BIT |
                ProjectX.COIN_BIT |
                ProjectX.BRICK_BIT |
                ProjectX.ENEMY_BIT |
                ProjectX.OBJECT_BIT |
                ProjectX.YASUO_BIT |
                ProjectX.MUSHROOM_BIT |
                ProjectX.TOXIC_BIT |
                ProjectX.FIREBALL_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-4f, 8.5f).scl(1 /ProjectX.PPM);
        vertice[1] = new Vector2(4f, 8.5f).scl(1 /ProjectX.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 /ProjectX.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 /ProjectX.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = ProjectX.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch){
        if (!destroyed || stateTime < 1) super.draw(batch);
    }

    @Override
    public void hitOnHead() {
        setToDesTroyed = true;
        ProjectX.manager.get("Audio/sounds/stomp.wav", Sound.class).play();
    }

    public void eatToxic(){ setToDesTroyed = true;}
}
