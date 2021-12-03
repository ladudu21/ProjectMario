package com.ladudu.projectx.sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.screens.PlayScreen;
import com.ladudu.projectx.sprites.Yasuo;

public class Mushroom extends Item{
    public Mushroom(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setBounds(getX(), getY(), 16 / ProjectX.PPM, 16 /ProjectX.PPM);
        setRegion(screen.getAtlas().findRegion("mushroom"), -1, 0 ,18, 18);
        velocity = new Vector2(0.5f, -1);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(4/ ProjectX.PPM);
        fdef.filter.categoryBits = ProjectX.MUSHROOM_BIT;
        fdef.filter.maskBits = ProjectX.YASUO_BIT |
                ProjectX.OBJECT_BIT |
                ProjectX.GROUND_BIT |
                ProjectX.COIN_BIT |
                ProjectX.BRICK_BIT |
                ProjectX.ENEMY_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Yasuo yasuo) {
        destroy();
        yasuo.transform();
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
