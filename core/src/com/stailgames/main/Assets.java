package com.stailgames.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

	private static final String TAG = Assets.class.getName();

	private AssetManager manager;

	public Assets() {
		manager = new AssetManager();
		manager.setErrorListener(this);
	}

	public void load() {
		manager.load("rocketroach.atlas", TextureAtlas.class);
		manager.load("freeky-32.fnt", BitmapFont.class);
		manager.load("freeky-64.fnt", BitmapFont.class);
		manager.load("theme-3.ogg", Music.class);
		// sound effect loader
		manager.load("hit.wav", Sound.class);
		manager.load("collect-coin.wav", Sound.class);
		manager.load("power-up.wav", Sound.class);
		manager.load("damage.mp3", Sound.class);
		manager.load("boost.mp3", Sound.class);
	}

	@Override
	public void error(@SuppressWarnings("rawtypes") AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset " + asset.fileName + " : " + (Exception) throwable);
	}

	@Override
	public void dispose() {
		manager.dispose();
		manager.clear();
		skin.dispose();
	}

	public boolean loading() {
		return !manager.update();
	}

	public float progress() {
		return manager.getProgress();
	}

	public AtlasRegion image(String filename) {
		return manager.get("rocketroach.atlas", TextureAtlas.class).findRegion(filename);
	}

	public BitmapFont font32() {
		return manager.get("freeky-32.fnt", BitmapFont.class);
	}

	public BitmapFont font64() {
		return manager.get("freeky-64.fnt", BitmapFont.class);
	}

	public Sound soundWav(String filename) {
		return manager.get(filename + ".wav", Sound.class);
	}

	public Sound soundMp3(String filename) {
		return manager.get(filename + ".mp3", Sound.class);
	}

	public Music music() {
		return manager.get("theme-3.ogg", Music.class);
	}

	public TextureRegion[][] images(String filename, int tileWidth, int tileHeight) {
		return fullimage().findRegion(filename).split(tileWidth, tileHeight);
	}

	private Skin skin = new Skin();

	public Drawable uiskin(String filename) {
		skin.addRegions(manager.get("rocketroach.atlas", TextureAtlas.class));
		return skin.getDrawable("ui/" + filename);
	}

	public TextureAtlas fullimage() {
		return manager.get("rocketroach.atlas", TextureAtlas.class);
	}

}