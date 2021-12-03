package com.ladudu.projectx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.ladudu.projectx.screens.PlayScreen;

public class ProjectX extends Game {
	public static final  int V_WIDTH = 400;
	public static final  int V_HEIGHT = 208;
	public static final float PPM = 100;
	public static SpriteBatch batch;

	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short ENEMY_HEAD_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROY_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short YASUO_BIT = 128;
	public static final short MUSHROOM_BIT = 256;
	public static final short YASUO_HEAD_BIT = 512;
	public static final short TOXIC_BIT = 1024;
	public static final short FIREBALL_BIT = 2048;

	public static AssetManager manager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		manager = new AssetManager();
		manager.load("Audio/sounds/coin.wav", Sound.class);
		manager.load("Audio/sounds/bump.wav", Sound.class);
		manager.load("Audio/sounds/breakblock.wav", Sound.class);
		manager.load("Audio/sounds/nope3.mp3", Sound.class);
		manager.load("Audio/sounds/powerup3.mp3", Sound.class);
		manager.load("Audio/sounds/stomp.wav", Sound.class);
		manager.load("Audio/sounds/eat3.mp3", Sound.class);
		manager.load("Audio/sounds/hurt3.mp3", Sound.class);
		manager.load("Audio/sounds/die3.mp3", Sound.class);
		manager.load("Audio/sounds/jump3.mp3", Sound.class);
		manager.load("Audio/sounds/gameover3.mp3", Sound.class);
		manager.load("Audio/sounds/toxic3.mp3", Sound.class);
		manager.load("Audio/sounds/glass3.mp3", Sound.class);
		manager.finishLoading();

		setScreen(new PlayScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}

	@Override
	public void render () {
		super.render();
	}
}
