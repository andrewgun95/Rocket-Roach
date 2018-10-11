package com.stailgames.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.components.Component;
import com.stailgames.main.R;
import com.stailgames.utils.Converter;

public class Smoke extends Component implements Poolable {

	public static final float WIDTH = Converter.pixelToMeter(32f);
	public static final float HEIGHT = Converter.pixelToMeter(24f);

	public Array<Sprite> sprites = new Array<Sprite>();

	public float x, y;
	public float duration;
	public boolean active;

	public Smoke() {
		this.active = false;
	}

	public void set(float x, float y, float duration) {
		this.x = x;
		this.y = y;
		this.duration = duration;
	}

	private float tick = .0f;
	private int counter = 0;
//	private double noise = .0f;

	public void active(boolean state){
		active = state;
	}
	
	@Override
	public void update(float delta) {

		if (active) {
			if ((tick += delta) > duration) {
				Sprite sprite = new Sprite(R.assets.image("character/smoke"));
//
//				float n = (float) noise(noise += 0.1, 0.0, 0.0);
//
//				float offsetY = map(n, -0.5f, 0.5f, -0.1f, 0.1f);

				sprite.setPosition(x, y + MathUtils.random(-0.1f, 0.1f));
				sprite.setSize(WIDTH * 2, HEIGHT * 2);

				sprite.setOriginCenter();
				sprite.setScale(.0f);

				sprites.add(sprite);
				tick = .0f;
				counter++;
			}
		}

		for (Sprite sprite : sprites) {
			sprite.setX(sprite.getX() - 0.01f);

			sprite.setOriginCenter();
			float scaleX = sprite.getScaleX() < 1 ? 0.1f : 0.0f;
			float scaleY = sprite.getScaleY() < 1 ? 0.1f : 0.0f;
			sprite.setScale(sprite.getScaleX() + scaleX, sprite.getScaleY() + scaleY);
		}

		if (sprites.size != 0) {
			Sprite first = sprites.first();
			first.setAlpha(first.getColor().a * 0.9f);

			if (counter == 2) {
				sprites.removeValue(first, false);
				counter = 0;
			}
		}

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (Sprite sprite : sprites) {
			sprite.draw(batch);
		}
	}

	@Override
	public void reset() {
	}

	static public double noise(double x, double y, double z) {
		// find unit cube that contains point.
		int X = (int) Math.floor(x) & 255, Y = (int) Math.floor(y) & 255, Z = (int) Math.floor(z) & 255;

		// find relative x,y,z of point in cube.
		x -= Math.floor(x);
		y -= Math.floor(y);
		z -= Math.floor(z);

		// compute fade curves for each of x,y,z.
		double u = fade(x), v = fade(y), w = fade(z);

		// hash coordinates of the 8 cube corners,
		int A = p[X] + Y, AA = p[A] + Z, AB = p[A + 1] + Z, B = p[X + 1] + Y, BA = p[B] + Z, BB = p[B + 1] + Z;

		return lerp(w,
				lerp(v, lerp(u, grad(p[AA], x, y, z), // and add
						grad(p[BA], x - 1, y, z)), // blended
				lerp(u, grad(p[AB], x, y - 1, z), // results
						grad(p[BB], x - 1, y - 1, z))), // from 8
				lerp(v, lerp(u, grad(p[AA + 1], x, y, z - 1), // corners
						grad(p[BA + 1], x - 1, y, z - 1)), // of cube
						lerp(u, grad(p[AB + 1], x, y - 1, z - 1), grad(p[BB + 1], x - 1, y - 1, z - 1))));
	}

	static double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	static double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}

	static double grad(int hash, double x, double y, double z) {
		// convert LO 4 bits of hash code
		int h = hash & 15;
		// into 12 gradient directions
		double u = h < 8 ? x : y, v = h < 4 ? y : h == 12 || h == 14 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}

	static final int p[] = new int[512], permutation[] = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148,
			247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146,
			158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135,
			130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
			223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228,
			251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254,
			138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180 };

	static {
		for (int i = 0; i < 256; i++)
			p[256 + i] = p[i] = permutation[i];
	}

	static float map(float value, float start1, float stop1, float start2, float stop2) {
		return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
	}

}
