package com.ladudu.projectx.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ladudu.projectx.ProjectX;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    Label levelLabel;
    Label worldLabel;
    Label yasuoLabel;

    public Hud(SpriteBatch sb){
        viewport = new FitViewport(ProjectX.V_WIDTH, ProjectX.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("World", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        yasuoLabel = new Label("Demo", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(yasuoLabel).expandX();
        table.add(worldLabel).expandX();
        table.row();
        table.add().expandX();
        table.add(levelLabel);

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
