package com.stailgames.components;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.gameobject.Movement;
import com.stailgames.main.R;

public class Heart extends Component implements Poolable {

	// UTILITIES
	private static void drawCenter(Batch batch, TextureRegion region, float x, float y, float width, float height) {
		batch.draw(region, x - width / 2, y - height / 2, width, height);
	}

	public static final float WIDTH = pixelToMeter(47);
	public static final float HEIGHT = pixelToMeter(40);

	private float x, y;
	private float scale, delay;

	public Heart() {
		super("Heart");
		x = 0.0f;
		y = 0.0f;
		scale = 0.0f;
	}

	public void create(float x, float y, float delay) {
		this.x = x;
		this.y = y;
		this.delay = delay;
		setBoundingRectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
	}

	private float tick = 0.0f;

	@Override
	public void update(float delta) {
		x += Movement.getSpeed().game;

		if ((tick += delta) > delay) {
			scale = scale > -1f ? scale - 0.1f : 1.0f;
			tick = 0.0f;
		}

		setBoundingRectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		drawCenter(batch, R.assets.image("component/heart_big"), x, y, WIDTH * scale, HEIGHT);
	}

	@Override
	public boolean equals(Object heart) {
		return this.x == ((Heart) heart).x && this.y == ((Heart) heart).y;
	}

	@Override
	public void reset() {
		x = 0.0f;
		y = 0.0f;
		scale = 0.0f;
	}

}
