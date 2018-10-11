package com.stailgames.components;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.gameobject.Movement;
import com.stailgames.main.R;

public class Battery extends Component implements Poolable {

	// utilities
	private static void drawCenter(Batch batch, TextureRegion region, float x, float y, float width, float height) {
		batch.draw(region, x - width / 2, y - height / 2, width, height);
	}

	public enum Model {
		SINGLE(pixelToMeter(15), pixelToMeter(50)), DOUBLE(pixelToMeter(27), pixelToMeter(50));

		private float width, height;

		private Model(float width, float height) {
			this.width = width;
			this.height = height;
		}

		public float getWidth() {
			return width;
		}

		public float getHeight() {
			return height;
		}
	}

	private float x, y;

	private Model model;

	public Battery() {
		super("Battery");
		x = 0.0f;
		y = 0.0f;
	}

	public void create(float x, float y, Model model) {
		this.x = x;
		this.y = y;

		this.model = model;

		setBoundingRectangle(x - model.getWidth() / 2, y - model.getHeight() / 2, model.getWidth(), model.getHeight());
	}

	@Override
	public void update(float delta) {
		x += Movement.getSpeed().game;

		setBoundingRectangle(x - model.getWidth() / 2, y - model.getHeight() / 2, model.getWidth(), model.getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		drawCenter(batch, model == Model.SINGLE ? R.assets.image("component/battery_blue-1") : R.assets.image("component/battery_yellow-2"), x, y, model.getWidth(), model.getHeight());
	}

	@Override
	public boolean equals(Object battery) {
		return this.x == ((Battery) battery).x && this.y == ((Battery) battery).y;
	}

	@Override
	public void reset() {
		x = 0.0f;
		y = 0.0f;
	}

	public Model getModel() {
		return model;
	}

}