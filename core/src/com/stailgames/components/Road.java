package com.stailgames.components;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.gameobject.Movement;
import com.stailgames.main.R;

public class Road extends Component implements Poolable {

	public static final float WIDTH = pixelToMeter(200);
	public static final float HEIGHT = pixelToMeter(50);

	public float x, y;
	public boolean flip;

	private float scaleX, scaleY;

	public Road() {
		super("Road");
		x = 0.0f;
		y = 0.0f;
		scaleX = 1.0f;
		scaleY = 1.0f;
		flip = false;
	}

	public void setScale(float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public void create(float x, float y) {
		create(x, y, false);
	}

	public void create(float x, float y, boolean flip) {
		this.x = x;
		this.y = y;
		this.flip = flip;

		setBoundingRectangle(x, y, WIDTH * scaleX, flip ? -(HEIGHT * scaleY) : (HEIGHT * scaleY));
	}

	@Override
	public void update(float delta) {
		x += Movement.getSpeed().game;
		setBoundingRectangle(x, y, WIDTH * scaleX, flip ? -(HEIGHT * scaleY) : (HEIGHT * scaleY));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(R.assets.image("component/road"), x, y, WIDTH * scaleX, flip ? -(HEIGHT * scaleY) : (HEIGHT * scaleY));
	}

	@Override
	public boolean equals(Object road) {
		return this.x == ((Road) road).x && this.y == ((Road) road).y;
	}

	@Override
	public void reset() {
		x = 0.0f;
		y = 0.0f;
		scaleX = 1.0f;
		scaleY = 1.0f;
	}

}
