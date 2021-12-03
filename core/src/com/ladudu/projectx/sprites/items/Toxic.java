package com.ladudu.projectx.sprites.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.screens.PlayScreen;
import com.ladudu.projectx.sprites.Yasuo;

public class Toxic extends Item {
    public Toxic(PlayScreen screen, float x, float y){
        super(screen, x, y);
        setBounds(getX(), getY(), 16 / ProjectX.PPM, 42 /ProjectX.PPM);
        setRegion(screen.getAtlas().findRegion("mushroom"), 22, -10 ,22, 42);
        velocity = new Vector2(1.2f, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-4, 18).scl(1 /ProjectX.PPM);
        vertice[1] = new Vector2(3.5f, 18).scl(1 /ProjectX.PPM);
        vertice[2] = new Vector2(-4, 0).scl(1 /ProjectX.PPM);
        vertice[3] = new Vector2(3.5f, 0).scl(1 /ProjectX.PPM);
        shape.set(vertice);

        fdef.filter.categoryBits = ProjectX.TOXIC_BIT;
        fdef.filter.maskBits = ProjectX.YASUO_BIT |
                ProjectX.OBJECT_BIT |
                ProjectX.GROUND_BIT |
                ProjectX.COIN_BIT |
                ProjectX.BRICK_BIT |
                ProjectX.ENEMY_BIT;

        fdef.shape = shape;
        fdef.restitution = 0.6f;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Yasuo yasuo) {
        ProjectX.manager.get("Audio/sounds/glass3.mp3", Sound.class).play();
        destroy();
        yasuo.hit();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            velocity.y = b2body.getLinearVelocity().y;
            b2body.setLinearVelocity(velocity);
        }
    }
}
