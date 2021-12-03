package com.ladudu.projectx.sprites;

import static com.ladudu.projectx.ProjectX.batch;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ladudu.projectx.ProjectX;
import com.ladudu.projectx.screens.PlayScreen;

public class Yasuo extends Sprite {
    public enum Stage {FALLING, JUMPING, STANDING, RUNNING, TRANSFORM, DEAD};
    public Stage curStage;
    public Stage preStage;

    public World world;
    public Body b2body;

    private TextureRegion yasuoStand;
    private Animation<TextureRegion> yasuoRun;
    private TextureRegion yasuoJump;

    private TextureRegion bigYasuoStand;
    private TextureRegion bigYasuoJump;
    private TextureRegion yasuoDead;
    private Animation<TextureRegion> bigYasuoRun;
    private Animation<TextureRegion> transformYasuo;

    private float stateTimer;
    private boolean runningRight;

    private boolean yasuoIsBig;
    private boolean runTransformAnimation;
    private boolean timeToDefineBigYasuo;
    private boolean timeToRedefineYasuo;
    private boolean yasuoIsDead;

    public Yasuo(PlayScreen screen){
        this.world = screen.getWorld();
        curStage = Stage.STANDING;
        preStage = Stage.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i=1; i<4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i*16, 0, 16, 16));
        yasuoRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        for (int i=1; i<4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0, 16, 32));
        bigYasuoRun = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 199, 0, 30, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 230, 0, 30, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 261, 0, 30, 32));
        transformYasuo = new Animation<TextureRegion>(0.25f, frames);
        frames.clear();

        yasuoJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
        bigYasuoJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);

        yasuoStand =  new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0,16, 16);
        bigYasuoStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

        yasuoDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0 ,16, 16);
        defineYasuo();

        setBounds(0,0,16/ ProjectX.PPM,16/ ProjectX.PPM);
        setRegion(yasuoStand);
    }

    public void update(float dt){
        if (yasuoIsBig) setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2 - 6 /ProjectX.PPM);
        else setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);

        setRegion(getFrame(dt));

        if (timeToDefineBigYasuo) defineBigYasuo();
        if (timeToRedefineYasuo) redefineYasuo();
    }

    public TextureRegion getFrame(float dt){
        curStage = getState();
        TextureRegion region;
        switch (curStage){
            case DEAD:
                region = yasuoDead;
                break;
            case TRANSFORM:
                region = transformYasuo.getKeyFrame(stateTimer);
                if (transformYasuo.isAnimationFinished(stateTimer)){
                    setBounds(getX(), getY(), 16/ ProjectX.PPM, 32/ProjectX.PPM);
                    runTransformAnimation = false;
                }
                break;
            case JUMPING:
                region = yasuoIsBig ? bigYasuoJump: yasuoJump;
                break;
            case RUNNING:
                region = yasuoIsBig ? bigYasuoRun.getKeyFrame(stateTimer, true) : yasuoRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = yasuoIsBig ? bigYasuoStand : yasuoStand;
                break;
        }
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = curStage == preStage ? stateTimer + dt : 0;
        preStage = curStage;
        return region;
    }

    public Stage getState(){
        if (yasuoIsDead || b2body.getPosition().y < 0) return Stage.DEAD;
        else if(runTransformAnimation)
            return Stage.TRANSFORM;
        else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && preStage == Stage.JUMPING))
            return Stage.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return Stage.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return Stage.RUNNING;
        else
            return Stage.STANDING;
    }

    public void transform(){
        if (!isBig()) {
            runTransformAnimation = true;
            yasuoIsBig = true;
            timeToDefineBigYasuo = true;
            setBounds(getX(), getY(), 32/ ProjectX.PPM, 32/ ProjectX.PPM);
            ProjectX.manager.get("Audio/sounds/powerup3.mp3", Sound.class).play();
        }
        else ProjectX.manager.get("Audio/sounds/eat3.mp3", Sound.class).play();
    }

    public void defineYasuo(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/ ProjectX.PPM, 32/ProjectX.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ ProjectX.PPM);
        fdef.filter.categoryBits = ProjectX.YASUO_BIT;
        fdef.filter.maskBits = ProjectX.GROUND_BIT |
                ProjectX.COIN_BIT |
                ProjectX.BRICK_BIT |
                ProjectX.ENEMY_BIT |
                ProjectX.OBJECT_BIT |
                ProjectX.ENEMY_HEAD_BIT |
                ProjectX.MUSHROOM_BIT |
                ProjectX.TOXIC_BIT ;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / ProjectX.PPM, 6 / ProjectX.PPM), new Vector2(2 / ProjectX.PPM, 6 / ProjectX.PPM));
        fdef.filter.categoryBits = ProjectX.YASUO_HEAD_BIT;
        fdef.shape = head;

        b2body.setBullet(true);
        b2body.createFixture(fdef).setUserData(this);
    }

    public void defineBigYasuo(){
        Vector2 curPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(curPosition.add(0, 10 /ProjectX.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(3/ ProjectX.PPM);
        fdef.filter.categoryBits = ProjectX.YASUO_BIT;
        fdef.filter.maskBits = ProjectX.GROUND_BIT |
                ProjectX.COIN_BIT |
                ProjectX.BRICK_BIT |
                ProjectX.ENEMY_BIT |
                ProjectX.OBJECT_BIT |
                ProjectX.ENEMY_HEAD_BIT |
                ProjectX.MUSHROOM_BIT |
                ProjectX.TOXIC_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setRadius(6 /ProjectX.PPM);
        shape.setPosition(new Vector2(0, -14 /ProjectX.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / ProjectX.PPM, 6 / ProjectX.PPM), new Vector2(2 / ProjectX.PPM, 6 / ProjectX.PPM));
        fdef.filter.categoryBits = ProjectX.YASUO_HEAD_BIT;
        fdef.shape = head;

        b2body.setBullet(true);
        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigYasuo = false;
    }

    public void hit(){
        if (yasuoIsBig){
            yasuoIsBig = false;
            timeToRedefineYasuo = true;
            setBounds(getX(), getY(), 16 / ProjectX.PPM, 16 / ProjectX.PPM);
            ProjectX.manager.get("Audio/sounds/hurt3.mp3", Sound.class).play();
        }
        else{
            ProjectX.manager.get("Audio/sounds/die3.mp3", Sound.class).play();
            yasuoIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = ProjectX.NOTHING_BIT;
            for (Fixture fixture: b2body.getFixtureList()) fixture.setFilterData(filter);
            b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
        }
    }

    public boolean isBig(){
        return yasuoIsBig;
    }

    public void redefineYasuo(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ ProjectX.PPM);
        fdef.filter.categoryBits = ProjectX.YASUO_BIT;
        fdef.filter.maskBits = ProjectX.GROUND_BIT |
                ProjectX.COIN_BIT |
                ProjectX.BRICK_BIT |
                ProjectX.ENEMY_BIT |
                ProjectX.OBJECT_BIT |
                ProjectX.ENEMY_HEAD_BIT |
                ProjectX.MUSHROOM_BIT |
                ProjectX.TOXIC_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / ProjectX.PPM, 6 / ProjectX.PPM), new Vector2(2 / ProjectX.PPM, 6 / ProjectX.PPM));
        fdef.filter.categoryBits = ProjectX.YASUO_HEAD_BIT;
        fdef.shape = head;

        b2body.setBullet(true);
        b2body.createFixture(fdef).setUserData(this);

        timeToRedefineYasuo = false;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void draw(Batch batch){
        super.draw(batch);
    }
}
