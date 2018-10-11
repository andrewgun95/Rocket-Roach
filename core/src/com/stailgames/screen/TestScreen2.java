package com.stailgames.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.stailgames.character.Smoke;
import com.stailgames.main.World;

public class TestScreen2 extends AbstractScreen {

	private Stage stage;

	public TestScreen2(Game game) {
		super(game);
	}

	Smoke smoke;

	@Override
	public void show() {
		stage = new Stage(new FillViewport(World.WIDTH, World.HEIGHT));
		smoke = new Smoke();
		smoke.set(2.5f, 1.5f, 0.1f);

		stage.addActor(smoke);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);

		if (Gdx.input.justTouched()) {
			smoke.active(!smoke.active);
		}

		stage.act(delta);
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void hide() {
	}

}
