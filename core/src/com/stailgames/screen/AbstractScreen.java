package com.stailgames.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.stailgames.main.R;

public abstract class AbstractScreen implements Screen {
	
	protected Game game;
	
	public AbstractScreen(Game game){
		this.game = game;
	}
	
	@Override
	abstract public void show();

	@Override
	abstract public void render(float delta);

	@Override
	abstract public void resize(int width, int height);

	@Override
	abstract public void pause();

	@Override
	public void resume() {
		R.assets.load();
	}

	@Override
	abstract public void hide();

	@Override
	public void dispose() {
		R.assets.dispose();
	}

}