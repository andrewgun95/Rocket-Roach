package com.stailgames.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Trail implements Poolable {

	public static class Config {

		public Color color = Color.WHITE;
		public TextureRegion image = null;
		public float width = 1.0f;
		public float height = 1.0f;
		public float respawn = 0.5f;
		public float alphadecs = 1.0f;
		public float scaledecs = 1.0f;

	}

	private Pool<Rectangle> poolRec = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle(config.color);
		}
	};
	private Array<Rectangle> recs = new Array<Rectangle>();

	private Config config;

	public float x, y;
	public Vector2 velocity;

	public boolean stop;

	public Trail(Config config) {
		this.config = config;
		x = 0.0f;
		y = 0.0f;
		stop = true;
		velocity = new Vector2();
	}

	public void create(float x, float y, Vector2 velocity) {
		this.x = x;
		this.y = y;
		this.velocity = velocity;
	}

	public void stop() {
		stop = true;
	}

	public void play() {
		stop = false;
	}

	private float tick = 0.0f;

	public void update(float delta) {
		// create
		if (!stop) {
			if ((tick += delta) > config.respawn) {
				Rectangle rec = poolRec.obtain();
				rec.create(x, y, velocity);
				recs.add(rec);
				tick = 0.0f;
			}
		}
		// update
		for (Rectangle r : recs) {
			r.act(delta);
		}
	}

	public void draw(Batch batch) {
		for (Rectangle r : recs) {
			r.draw(batch);
		}
	}

	@Override
	public void reset() {
		x = 0.0f;
		y = 0.0f;
		stop = false;
	}

	public class Rectangle extends Sprite implements Poolable {

		private Texture createTexture(int bit, Color color) {
			Pixmap pixmap = new Pixmap(bit, bit, Format.RGBA8888);
			pixmap.setColor(color);
			pixmap.fill();
			return new Texture(pixmap);
		}

		public float scale, alpha;
		public Vector2 velocity;

		public Rectangle(Color color) {
			// set texture
			if (config.image == null) 
				setTexture(createTexture(32, color));
			else
				setRegion(config.image);
			setSize(config.width, config.height);
			// initial
			scale = 0.0f;
			alpha = 0.0f;
			velocity = new Vector2();
		}

		public void create(float x, float y, Vector2 velocity) {
			setPosition(x, y);
			this.velocity = velocity;
			this.scale = 1.0f;
			this.alpha = 1.0f;
		}

		private float tick = 0.0f;

		public void act(float delta) {

			// update
			setPosition(getX() + velocity.x, getY() + velocity.y);
			if ((tick += delta) > 0.1f) {

				alpha = alpha > 0.2f ? alpha - config.alphadecs : 0.0f;
				scale = scale > 0.2f ? scale - config.scaledecs : 0.0f;

				tick = 0.0f;
			}
			setAlpha(alpha);
			setScale(scale);

			// destroy
			if (alpha == 0.0f) {
				recs.removeValue(this, false);
				poolRec.free(this);
			}
		}

		public void draw(Batch batch) {
			super.draw(batch);
		}

		@Override
		public void reset() {
			scale = 0.0f;
			alpha = 0.0f;
		}

		@Override
		public boolean equals(Object rec) {
			return getX() == ((Sprite) rec).getX() && getY() == ((Sprite) rec).getY();
		}

	}
}
