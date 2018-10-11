package com.stailgames.utils;

import com.badlogic.gdx.math.MathUtils;

public class Randomize {
	// utilities
	public static float percent(float amount) {
		return amount / 100;
	}

	/**
	 * @param chance
	 *            in percent
	 * @return
	 */
	public static boolean random(int percent) {
		return MathUtils.randomBoolean(percent(percent));
	}

	/**
	 * random no even value
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int randomOdd(int start, int end) {
		int random = MathUtils.random(start, end);
		while (random % 2 == 0)
			random = MathUtils.random(start, end);
		return random;
	}

	public static int random(int start, int end) {
		return MathUtils.random(start, end);
	}
	
	public static float random(float start, float end){
		return MathUtils.random(start, end);
	}

	public static int random(int... value) {
		return value[MathUtils.random(0, value.length - 1)];
	}

	public static float random(float... value) {
		return value[MathUtils.random(0, value.length - 1)];
	}
}
