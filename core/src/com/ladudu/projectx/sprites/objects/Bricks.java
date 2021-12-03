package com.ladudu.projectx.sprites.objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.screens.PlayScreen;
import com.ladudu.projectx.sprites.Yasuo;

public class Bricks extends InteractiveTileObject {
    public Bricks(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(ProjectX.BRICK_BIT);
    }
    @Override
    public void onHeadHit(Yasuo yasuo) {
        if (yasuo.isBig()) {
            setCategoryFilter(ProjectX.DESTROY_BIT);
            getCell().setTile(null);

            ProjectX.manager.get("Audio/sounds/breakblock.wav", Sound.class).play();
        }
        else ProjectX.manager.get("Audio/sounds/bump.wav", Sound.class).play();
    }
}
