package com.ladudu.projectx.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.screens.PlayScreen;
import com.ladudu.projectx.sprites.objects.Bricks;
import com.ladudu.projectx.sprites.objects.Coins;
import com.ladudu.projectx.sprites.enemies.Goomba;
import com.ladudu.projectx.sprites.objects.Pipes;
import com.ladudu.projectx.sprites.objects.Springs;

public class B2WorldCreator {
    private Array<Goomba> goombas;
    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            new Bricks(screen, object);
        }

        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            new Coins(screen, object);
        }

        for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ProjectX.PPM, (rect.getY() + rect.getHeight()/2)/ProjectX.PPM);
            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2 / ProjectX.PPM, rect.getHeight()/2 /ProjectX.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for (MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            new Pipes(screen, object);
        }

        for (MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            new Springs(screen, object);
        }

        goombas = new Array<Goomba>();
        for (MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() /ProjectX.PPM, rect.getY() /ProjectX.PPM));
        }
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }
}
