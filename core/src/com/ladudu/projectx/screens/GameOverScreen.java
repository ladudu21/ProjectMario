package com.ladudu.projectx.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ladudu.projectx.ProjectX;

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private Game game;

    public GameOverScreen(Game game){
        ProjectX.manager.get("Audio/sounds/gameover3.mp3", Sound.class).play();
        this.game = game;
        viewport = new FitViewport(ProjectX.V_WIDTH, ProjectX.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((ProjectX) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOver = new Label("Game Over", font);
        Label playAgain = new Label("Play Again", font);

        table.add(gameOver).expandX();
        table.row();
        table.add(playAgain).expandX().padTop(10f);

        stage.addActor(table);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()){
            ProjectX.manager.get("Audio/sounds/gameover3.mp3", Sound.class).stop();
            game.setScreen(new PlayScreen((ProjectX) game));
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        dispose();
    }
}
