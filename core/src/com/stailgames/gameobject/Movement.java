package com.stailgames.gameobject;

public class Movement {

	public static class Speed {

		public float game;
		public float charging;
		public float throwing;
		public float wallmotion;

		public Speed() {
		}

		public Speed(float game, float charging, float throwing, float wallmotion) {
			this.game = game;
			this.charging = charging;
			this.throwing = throwing;
			this.wallmotion = wallmotion;
		}
	}

	private static Speed defspeed;
	private static Speed speed;

	public static void init(Speed speed) {
		Movement.speed = speed;
		defspeed = speed;
	}

	public static void speed(float times) {
		speed.game = speed.game * times;
		speed.charging = speed.charging * times;
		speed.throwing = speed.throwing * times;
		speed.wallmotion = speed.wallmotion * times;
	}

	public static void stopAll() {
		speed = new Speed();
	}

	public static void playAll() {
		speed = defspeed;
	}

	public static Speed getSpeed() {
		return speed;
	}

}