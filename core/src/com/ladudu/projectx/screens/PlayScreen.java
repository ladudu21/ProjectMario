package com.ladudu.projectx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.scenes.Hud;
import com.ladudu.projectx.sprites.Others.FireBall;
import com.ladudu.projectx.sprites.enemies.Enemy;
import com.ladudu.projectx.sprites.Yasuo;
import com.ladudu.projectx.sprites.items.Item;
import com.ladudu.projectx.sprites.items.ItemDef;
import com.ladudu.projectx.sprites.items.Mushroom;
import com.ladudu.projectx.sprites.items.Toxic;
import com.ladudu.projectx.tools.B2WorldCreator;
import com.ladudu.projectx.tools.WorldContactListener;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen{
    private TextureAtlas atlas;

    private Yasuo player;

    private ProjectX game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private  B2WorldCreator creator;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    private Array<FireBall> fireBalls;

    Controller controller;

    public PlayScreen(ProjectX game){
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        gameCam = new OrthographicCamera();

        gamePort = new FitViewport(ProjectX.V_WIDTH/ ProjectX.PPM, ProjectX.V_HEIGHT/ ProjectX.PPM,  gameCam);

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("untitled.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / ProjectX.PPM);

        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        world = new World(new Vector2(0,-10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Yasuo(this);

        world.setContactListener(new WorldContactListener());

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
        fireBalls = new Array<FireBall>();

        controller = new Controller();

    }

    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItem(){
        if (!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
            if (idef.type == Toxic.class){
                items.add(new Toxic(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if (player.curStage != Yasuo.Stage.DEAD) {
            if (controller.isUpPressed() && player.b2body.getLinearVelocity().y == 0) {
                controller.upPressed = false;
                ProjectX.manager.get("Audio/sounds/jump3.mp3", Sound.class).play();
                player.b2body.applyLinearImpulse(new Vector2(0, 3), player.b2body.getWorldCenter(), true);
            }
            if (controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 1)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -1)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && player.isBig()) {
                Gdx.app.log("add", "fireball");
                fireBalls.add(new FireBall(this, player.b2body.getPosition().x , player.b2body.getPosition().y));
            }
        }
    public void update(float dt){
        handleInput(dt);
        handleSpawningItem();

        world.step(1/60f, 8, 3);

        player.update(dt);

        for (Enemy enemy: creator.getGoombas()){
            enemy.update(dt);
            if (enemy.getX() < player.getX() +  224/ ProjectX.PPM)
                enemy.b2body.setActive(true);
        }
        for (Item item: items) item.update(dt);
        for (FireBall ball: fireBalls) ball.update(dt);

        if (player.curStage != Yasuo.Stage.DEAD) gameCam.position.x = player.b2body.getPosition().x;

        gameCam.position.x = player.b2body.getPosition().x;
        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        //b2dr.render(world, gameCam.combined);

        controller.draw();

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy: creator.getGoombas()) enemy.draw(game.batch);
        for (Item item: items) item.draw(game.batch);
        for (FireBall ball : fireBalls) ball.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (isGameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public boolean isGameOver(){
        if (player.curStage == Yasuo.Stage.DEAD && player.getStateTimer() > 2){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        controller.resize(width, height);
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
