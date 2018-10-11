package com.stailgames.utils;

import static com.stailgames.main.World.PIXEL_PER_METER;

import com.badlogic.gdx.math.Vector2;

public class Converter {

	public static float pixelToMeter(float pixel) {
		return pixel / PIXEL_PER_METER;
	}

	public static float meterToPixel(float meter) {
		return meter * PIXEL_PER_METER;
	}

	public static float[] pixelToMeter(float[] pixel) {
		for (int i = 0; i < pixel.length; i++) {
			pixel[i] = pixel[i] / PIXEL_PER_METER;
		}
		return pixel;
	}

	public static Vector2 pixelToMeter(Vector2 pixel) {
		return pixel.set(pixel.x / PIXEL_PER_METER, pixel.y / PIXEL_PER_METER);
	}

	public static float colorByteToFloat(int colorBytes) {
		return colorBytes / 255f;
	}

}