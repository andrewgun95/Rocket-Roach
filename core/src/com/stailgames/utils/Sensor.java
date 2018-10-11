package com.stailgames.utils;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;

public class Sensor {

	private Polygon source;
	private Array<Target> targets = new Array<Target>();
	
	public Sensor(Polygon source, Polygon... target) {
		this.source = source;
		for (int i = 0; i < target.length; i++) {
			targets.add(new Target(target[i]));
		}
	}

	public boolean hit() {
		if (isOverlap() && isHit()) {
			return true;
		}
		return false;
	}

	private boolean isHit() {
		for (Target target : targets) {
			if (target.getHit()) {
				// is already hit
				target.hit(false);
				return true;
			}
		}
		return false;
	}

	private boolean isOverlap() {
		for (Target target : targets) {
			if (Intersector.overlapConvexPolygons(source, target.getBound())) {
				target.hit(true);
				return true;
			}
			target.hit(false);
		}
		return false;
	}

	private class Target {

		private boolean hit;
		private Polygon bound;

		private Target(Polygon bound) {
			this.bound = bound;
			hit = false;
		}

		private void hit(boolean state) {
			hit = state;
		}

		private boolean getHit() {
			return hit;
		}

		private Polygon getBound() {
			return bound;
		}

	}

}
