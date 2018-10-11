package com.stailgames.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.stailgames.screen.SplashScreen;

public class Main extends Game {

	private static boolean LOADING = true;

	public static String TAG = Main.class.getName();
	
	public static String NAME = "Rocket Roach";
	public static int VERSION = 1;

	private Assets assets;

	@Override
	public void create() {
		assets = new Assets();
		assets.load();
		
		if (!LOADING) {
			while (assets.loading()) {
				Gdx.app.log(TAG, "Loading : " + assets.progress());
			}
			Gdx.app.log(TAG, "Done");
			// located the assets
		}
		
		R.provide(assets);
		setScreen(new SplashScreen(this));
	}

}
