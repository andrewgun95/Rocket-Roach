package com.stailgames.components;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.gameobject.Movement;
import com.stailgames.main.R;

public class Walls extends Component implements Poolable {

	// UTILITIES
	private static void drawCenter(Batch batch, TextureRegion reg, float x, float y, float width, float height) {
		batch.draw(reg, x - width / 2, y - height / 2, width, height);
	}

	public static enum POSITION {

		UPPER(pixelToMeter(340)), MIDDLE(pixelToMeter(240)), BOTTOM(pixelToMeter(140));

		private float value;

		private POSITION(float value) {
			this.value = value;
		}

		public static POSITION random() {
			return values()[MathUtils.random(0, values().length - 1)];
		}

		public float getValue() {
			return value;
		}

	}

	public static final float GAP = pixelToMeter(150);

	private float x, y;

	public Wall roof;
	public Wall ground;

	// WALL MOTION
	public boolean motion;
	public float minY, maxY;
	public float duration;

	public Walls() {
		super("Walls");
		x = 0.0f;
		y = 0.0f;
		motion = false;
		minY = 0.0f;
		maxY = 0.0f;
		duration = 0.1f;

		roof = new Wall();
		ground = new Wall();
	}

	public void create(float x, float y) {
		this.x = x;
		this.y = y;

		// ROOF
		roof.create(x, y + GAP / 2 + Wall.HEIGHT / 2, true);
		// GROUND
		ground.create(x, y - GAP / 2 - Wall.HEIGHT / 2, false);

		setBoundingRectangle(x - Wall.WIDTH / 2, y - GAP / 2 - Wall.HEIGHT, Wall.WIDTH, Wall.HEIGHT * 2 + GAP);
	}

	public void update(float delta) {
		if (motion) {
			y = MathUtils.clamp(y + duration, minY, maxY);
			if (y == minY || y == maxY) duration = -duration;
		}
		// UPDATE POSITION
		x = roof.position(y + GAP / 2 + Wall.HEIGHT / 2);
		roof.update(delta);

		x = ground.position(y - GAP / 2 - Wall.HEIGHT / 2);
		ground.update(delta);

		setBoundingRectangle(x - Wall.WIDTH / 2, y - GAP / 2 - Wall.HEIGHT, Wall.WIDTH, Wall.HEIGHT * 2 + GAP);
	}

	public void draw(Batch batch, float parentAlpha) {
		roof.draw(batch, parentAlpha);
		ground.draw(batch, parentAlpha);
	}

	public void setMotion(float minY, float maxY, float duration) {
		motion = true;
		this.minY = minY;
		this.maxY = maxY;
		this.duration = duration;
	}

	@Override
	public void reset() {
		x = 0.0f;
		y = 0.0f;
		y = 0.0f;
		motion = false;
		minY = 0.0f;
		maxY = 0.0f;
		duration = 0.1f;
	}

	public Array<Wall> get() {
		Array<Wall> walls = new Array<Wall>();
		walls.addAll(roof, ground);
		return walls;
	}

	public static class Wall extends Component implements Poolable {

		public static final float WIDTH = pixelToMeter(60);
		public static final float HEIGHT = pixelToMeter(300);

		public float x;
		public float y;
		public boolean flip;

		public Wall() {
			super("Wall");
			x = 0.0f;
			y = 0.0f;
		}

		public void create(float x, float y, boolean flip) {
			this.x = x;
			this.y = y;
			this.flip = flip;
			final float boundWidth = WIDTH - 0.2f;
			setBoundingRectangle(x - boundWidth / 2, y - (flip ? -HEIGHT : HEIGHT) / 2, boundWidth, flip ? -HEIGHT : HEIGHT);
		}

		public float position(float y) {
			this.y = y;
			return x;
		}

		@Override
		public void update(float delta) {
			x += Movement.getSpeed().game;
			final float boundWidth = WIDTH - 0.2f;
			setBoundingRectangle(x - boundWidth / 2, y - (flip ? -HEIGHT : HEIGHT) / 2, boundWidth, flip ? -HEIGHT : HEIGHT);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			drawCenter(batch, R.assets.image("component/wall"), x, y, WIDTH, flip ? -HEIGHT : HEIGHT);
		}

		@Override
		public boolean equals(Object wall) {
			return this.x == ((Wall) wall).x && this.y == ((Wall) wall).y;
		}

		@Override
		public void reset() {
			x = 0.0f;
			y = 0.0f;
		}
	}

}
