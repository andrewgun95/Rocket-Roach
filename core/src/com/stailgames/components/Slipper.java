package com.stailgames.components;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.gameobject.Movement;
import com.stailgames.main.R;

public class Slipper extends Component implements Poolable {

	// UTILITIES
	private static void drawCenter(Batch batch, TextureRegion region, float x, float y, float width, float height, float rotation) {
		batch.draw(region, x - width / 2, y - height / 2, width / 2, height / 2, width, height, 1f, 1f, rotation);
	}

	public static final int TILEWIDTH = 61;
	public static final int TILEHEIGHT = 30;
	public static final float WIDTH = pixelToMeter(61);
	public static final float HEIGHT = pixelToMeter(30);

	public float x, y;
	public float rotate;

	public int frame;

	public Slipper() {
		super("Slipper");
		x = 0.0f;
		y = 0.0f;
	}

	public void create(float x, float y, float rotate) {
		this.x = x;
		this.y = y;
		this.rotate = rotate;

		setBoundingRectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
	}

	private float angle = 0.0f;
	private float tick = 0.0f;

	@Override
	public void update(float delta) {
		x += Movement.getSpeed().throwing;
		angle += rotate;

		setBoundingRectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
		setBoundRotation(angle);
		// update animation
		if ((tick += delta) > 0.1f) {
			frame++;
			tick = 0.0f;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		TextureRegion[] images = R.assets.images("component/slipper", TILEWIDTH, TILEHEIGHT)[0];
		drawCenter(batch, images[frame % images.length], x, y, WIDTH, HEIGHT, angle);
	}

	@Override
	public boolean equals(Object slipper) {
		return this.x == ((Slipper) slipper).x && this.y == ((Slipper) slipper).y;
	}

	@Override
	public void reset() {
		x = 0.0f;
		y = 0.0f;
	}

}
