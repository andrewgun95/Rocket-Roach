package com.stailgames.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.stailgames.main.Main;
import com.stailgames.main.World;

public class DesktopLauncher {

	private static final boolean BUILT = false;

	public static void main(String[] arg) {
		if (BUILT) {
			Settings settings = new Settings();
			settings.maxWidth = 2048;
			settings.maxHeight = 2048;
			settings.minWidth = 512;
			settings.minHeight = 512;
			settings.filterMin = TextureFilter.Nearest;
			settings.filterMag = TextureFilter.Nearest;
			settings.paddingX = 4;
			settings.paddingY = 4;
			settings.debug = false;
			TexturePacker.process(settings, "assets-raw", "C:/Users/Andreas/git/rocket-roach/Rocket-Roach/android/assets", "rocketroach");
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = World.getScreenWidth();
		config.height = World.getScreenHeight();
		config.resizable = false;
		new LwjglApplication(new Main(), config);
	}
}