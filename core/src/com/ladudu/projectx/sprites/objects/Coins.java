package com.ladudu.projectx.sprites.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.screens.PlayScreen;
import com.ladudu.projectx.sprites.Yasuo;
import com.ladudu.projectx.sprites.items.ItemDef;
import com.ladudu.projectx.sprites.items.Mushroom;
import com.ladudu.projectx.sprites.items.Toxic;

public class Coins extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coins(PlayScreen screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(ProjectX.COIN_BIT);
    }

    @Override
    public void onHeadHit(Yasuo yasuo){
        if (getCell().getTile().getId() == BLANK_COIN)
            ProjectX.manager.get("Audio/sounds/bump.wav", Sound.class).play();
        else {
            if (object.getProperties().containsKey("mushroom")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 /ProjectX.PPM),
                        Mushroom.class));
                ProjectX.manager.get("Audio/sounds/coin.wav", Sound.class).play();
            }
            else if (object.getProperties().containsKey("toxic")){
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 /ProjectX.PPM),
                        Toxic.class));
                ProjectX.manager.get("Audio/sounds/toxic3.mp3", Sound.class).play();
            }
            else ProjectX.manager.get("Audio/sounds/nope3.mp3", Sound.class).play();
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
    }
}
