package com.stailgames.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.gameobject.Movement;
import com.stailgames.main.R;

import static com.stailgames.utils.Converter.*;

public class Boxes extends Component implements Poolable {

	private static final float WIDTH = pixelToMeter(60);
	private static final float HEIGHT = pixelToMeter(60);

	private float x, y;
	private int unit;

	public Boxes() {
		super("Boxes");
	}

	public void create(float x, float y, int unit) {
		this.x = x;
		this.y = y;
		this.unit = unit;

		setBoundingRectangle(x - (WIDTH * unit) / 2, y - HEIGHT / 2, WIDTH * unit, HEIGHT);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float start = x - getBoundWidth() / 2;
		for (int i = 0; i < unit; i++) {
			batch.draw(R.assets.image("component/box"), start + (WIDTH * i), y - HEIGHT / 2, WIDTH, HEIGHT);
		}
	}

	@Override
	public void update(float delta) {
		x += Movement.getSpeed().game;
		setBoundingRectangle(x - (WIDTH * unit) / 2, y - HEIGHT / 2, WIDTH * unit, HEIGHT);
	}

	@Override
	public void reset() {
		x = 0.0f;
		y = 0.0f;
		unit = 0;
	}

}
