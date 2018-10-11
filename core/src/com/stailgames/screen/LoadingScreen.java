package com.stailgames.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.stailgames.main.R;
import com.stailgames.main.World;

public class LoadingScreen extends AbstractScreen {

	private static final String TAG = LoadingScreen.class.getName();

	private OrthographicCamera camera;
	private FillViewport viewport;
	private SpriteBatch batch;

	private BitmapFont font;

	private String message;
	private float duration;
	private int counter;

	public LoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		message = "loading";
		camera = new OrthographicCamera();
		viewport = new FillViewport(World.getScreenWidth(), World.getScreenHeight(), camera);
		camera.position.set(World.getScreenWidth() / 2, World.getScreenHeight() / 2, 0);
		camera.update();

		batch = new SpriteBatch();

		font = new BitmapFont(Gdx.files.internal("freeky-64.fnt"));

		counter = 0;
		duration = 1.0f;
	}

	private float tick = 0.0f;

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);

		if ((tick += delta) > duration) {
			counter++;
			message = message + ".";
			if (counter == 4) {
				message = "loading";
				counter = 0;
			}
			tick = 0.0f;
		}
		
		R.assets.load();
		
		if (R.assets.loading()) {
			Gdx.app.log(TAG, "Loading : " + R.assets.progress());
		} else {
			Gdx.app.log(TAG, "Done");
			game.setScreen(new GameScreen(game));
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.draw(batch, message, 250f, 50f);
		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void hide() {

	}

}
