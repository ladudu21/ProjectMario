package com.ladudu.projectx.sprites.objects;

import com.badlogic.gdx.maps.MapObject;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.screens.PlayScreen;
import com.ladudu.projectx.sprites.Yasuo;

public class Pipes extends InteractiveTileObject{
    public Pipes(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(ProjectX.OBJECT_BIT);
    }

    @Override
    public void onHeadHit(Yasuo yasuo) {

    }
}
