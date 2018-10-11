package com.stailgames.main;

public class World {

	// in meter
	public static final float WIDTH = 5f;
	public static final float HEIGHT = 3f;
	public static final float PIXEL_PER_METER = 160f;

	public static final float GRAVITY = -9.81f;

	public static final float getGravity() {
		return (GRAVITY * 0.1f * 0.1f);
	}

	public static final int getScreenWidth() {
		return (int) (WIDTH * PIXEL_PER_METER);
	}

	public static final int getScreenHeight() {
		return (int) (HEIGHT * PIXEL_PER_METER);
	}

}
