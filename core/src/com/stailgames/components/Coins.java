package com.stailgames.components;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;
import com.stailgames.gameobject.Movement;
import com.stailgames.main.R;

public class Coins extends Component {

	public enum Pattern {
		LINE, CURVE;

		public static Pattern random() {
			return values()[MathUtils.random(0, values().length - 1)];
		}
	}

	private Array<Coin> coins = new Array<Coin>();
	private float x, y;
	public int unit;
	public float offset;

	public Coins() {
		super("Coins");
	}

	public void create(float x, float y, int unit, float offset, Pattern pattern) {
		// SET
		this.x = x;
		this.y = y;
		this.unit = unit;
		this.offset = offset;
		
		final float boundWidth = (Coin.WIDTH * unit + offset * (unit - 1));
		final float boundHeight = Coin.HEIGHT;
				
		float xCoin = x - boundWidth / 2;
		float yCoin = y - Coin.HEIGHT;

		switch (pattern) {
		case LINE:
			for (int i = 0; i < unit; i++) {
				Coin coin = Pools.get(Coin.class).obtain();
				coin.create(xCoin, yCoin, 0.1f);
				coins.add(coin);
				xCoin += (Coin.WIDTH + offset);
			}
			break;
		case CURVE:
			for (int i = 0; i < unit; i++) {
				Coin coin = Pools.get(Coin.class).obtain();
				coin.create(xCoin, yCoin, 0.1f);
				coins.add(coin);
				xCoin += (Coin.WIDTH + offset);
				yCoin += (i == 0) ? offset : (i == unit - 2) ? -offset : 0.0f;
			}
			break;
		}

		setBoundingRectangle(x - boundWidth / 2, y - boundHeight / 2, boundWidth, boundHeight);
	}

	@Override
	public void update(float delta) {
		x += Movement.getSpeed().game;

		for (Coin coin : coins) {
			coin.update(delta);
		}
		
		final float boundWidth = (Coin.WIDTH * unit + offset * (unit - 1));
		final float boundHeight = Coin.HEIGHT;
		
		setBoundingRectangle(x - boundWidth / 2, y - boundHeight / 2, boundWidth, boundHeight);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (Coin coin : coins) {
			coin.draw(batch, parentAlpha);
		}
	}

	public Array<Coin> get() {
		return coins;
	}
	
	@Override
	public boolean equals(Object coin) {
		return this.x == ((Coins) coin).x && this.y == ((Coins) coin).y;
	}

	public static class Coin extends Component implements Poolable {

		public static final float WIDTH = pixelToMeter(50);
		public static final float HEIGHT = pixelToMeter(50);

		public float x, y;
		public float scale, delay;

		public Coin() {
			super("Coin");
			x = 0.0f;
			y = 0.0f;
			scale = 1.0f;
			delay = 0.0f;
		}

		private void create(float x, float y, float delay) {
			this.x = x;
			this.y = y;
			this.delay = delay;

			setBoundingRectangle(x, y, WIDTH, HEIGHT);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(R.assets.image("component/coin"), x, y, WIDTH / 2, HEIGHT / 2, WIDTH, HEIGHT, scale, 1.0f, 0.0f);
		}

		private float tick = 0.0f;

		@Override
		public void update(float delta) {
			x += Movement.getSpeed().game;

			if ((tick += delta) > delay) {
				scale = scale > -1f ? scale - 0.1f : 1.0f;
				tick = 0.0f;
			}

			setBoundingRectangle(x, y, WIDTH, HEIGHT);
		}

		@Override
		public boolean equals(Object coin) {
			return this.x == ((Coin) coin).x && this.y == ((Coin) coin).y;
		}

		@Override
		public void reset() {
			x = 0.0f;
			y = 0.0f;
			scale = 1.0f;
			delay = 0.0f;
		}

	}
}
