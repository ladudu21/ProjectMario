package com.ladudu.projectx.tools;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.sprites.Others.FireBall;
import com.ladudu.projectx.sprites.Yasuo;
import com.ladudu.projectx.sprites.enemies.Enemy;
import com.ladudu.projectx.sprites.items.Item;
import com.ladudu.projectx.sprites.objects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cdef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cdef){
            case ProjectX.YASUO_HEAD_BIT | ProjectX.BRICK_BIT:
            case ProjectX.YASUO_HEAD_BIT | ProjectX.COIN_BIT:
                if (fixA.getFilterData().categoryBits == ProjectX.YASUO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Yasuo) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Yasuo) fixB.getUserData());
                break;
            case ProjectX.ENEMY_HEAD_BIT | ProjectX.YASUO_BIT:
                if (fixA.getFilterData().categoryBits == ProjectX.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case ProjectX.ENEMY_BIT | ProjectX.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == ProjectX.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case ProjectX.YASUO_BIT | ProjectX.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == ProjectX.YASUO_BIT)
                    ((Yasuo) fixA.getUserData()).hit();
                else
                    ((Yasuo) fixB.getUserData()).hit();
                break;
            case ProjectX.ENEMY_BIT | ProjectX.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case ProjectX.MUSHROOM_BIT | ProjectX.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == ProjectX.MUSHROOM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case ProjectX.MUSHROOM_BIT | ProjectX.YASUO_BIT:
                if (fixA.getFilterData().categoryBits == ProjectX.MUSHROOM_BIT)
                    ((Item)fixA.getUserData()).use((Yasuo) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Yasuo) fixA.getUserData());
                break;
            case ProjectX.MUSHROOM_BIT | ProjectX.ENEMY_BIT:
                ProjectX.manager.get("Audio/sounds/eat3.mp3", Sound.class).play();
                if (fixA.getFilterData().categoryBits == ProjectX.MUSHROOM_BIT)
                    ((Item)fixA.getUserData()).destroy();
                else
                    ((Item)fixB.getUserData()).destroy();
                break;
            case ProjectX.TOXIC_BIT | ProjectX.ENEMY_BIT:
                ProjectX.manager.get("Audio/sounds/glass3.mp3", Sound.class).play();
                if (fixA.getFilterData().categoryBits == ProjectX.TOXIC_BIT) {
                    ((Item) fixA.getUserData()).destroy();
                    ((Enemy) fixB.getUserData()).eatToxic();
                }
                else {
                    ((Item) fixB.getUserData()).destroy();
                    ((Enemy) fixA.getUserData()).eatToxic();
                }
                break;
            case ProjectX.TOXIC_BIT | ProjectX.YASUO_BIT:
                if (fixA.getFilterData().categoryBits == ProjectX.TOXIC_BIT)
                    ((Item)fixA.getUserData()).use((Yasuo) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Yasuo) fixA.getUserData());
                break;
            case ProjectX.TOXIC_BIT | ProjectX.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == ProjectX.TOXIC_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case ProjectX.FIREBALL_BIT | ProjectX.ENEMY_BIT:
                ProjectX.manager.get("Audio/sounds/glass3.mp3", Sound.class).play();
                if (fixA.getFilterData().categoryBits == ProjectX.FIREBALL_BIT) {
                    ((FireBall) fixA.getUserData()).destroy();
                    ((Enemy) fixB.getUserData()).eatToxic();
                }
                else {
                    ((FireBall) fixB.getUserData()).destroy();
                    ((Enemy) fixA.getUserData()).eatToxic();
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
