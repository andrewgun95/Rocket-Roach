package com.stailgames.gameobject;

import com.badlogic.gdx.math.Vector2;
import com.stailgames.character.Cockroach;
import com.stailgames.components.Battery;
import com.stailgames.components.Battery.Model;
import com.stailgames.components.Boxes;
import com.stailgames.components.Coins.Coin;
import com.stailgames.components.Cones;
import com.stailgames.components.Heart;
import com.stailgames.components.Slipper;
import com.stailgames.components.Walls.Wall;
import com.stailgames.components.Windmill;
import com.stailgames.debug.Info;
import com.stailgames.main.R;

import static com.badlogic.gdx.math.Intersector.*;

public class Detector {

	// player
	private Cockroach cockroach;
	// game object
	private Environment environment;
	private Platform platform;
	private ThrowingObject object;

	// user interface
	private UI ui;

	private boolean active;

	public Detector(Cockroach cockroach, Environment environment, Platform platform, UI ui, ThrowingObject object) {
		this.cockroach = cockroach;
		this.environment = environment;
		this.platform = platform;
		this.object = object;
		this.ui = ui;

		active = true;
	}

	public void response(float delta) {
		if (!active) return;

		responseObstacle();
		responseItem();
		// is dead
		if (ui.getLive().getAmount() == 0) {
			active = false;
		} else {
			calculateDistance(delta, 1.0f);
			responsePlatform();
		}
	}

	public void reset() {
		active = true;
	}

	public boolean isActive() {
		return active;
	}

	private float tick = 0.0f;

	private void calculateDistance(float delta, float duration) {
		if ((tick += delta) > duration) {
			ui.getUIDistance().increase();

			if (ui.getUIDistance().getDistance() % 25 == 0) {
				Movement.speed(1.1f);
			}

			tick = 0.0f;
		}
	}

	private void responseObstacle() {
		responseWall();
		responseSlipper();
		responseWindmill();
		responseCone();
	}

	private void responseItem() {
		responseHeart();
		responseBattery();
		responseCoin();
	}

	private void responsePlatform() {
		for (Boxes boxes : environment.getBoxes()) {
			Vector2 p1 = new Vector2(boxes.getBoundX(), boxes.getBoundY() + boxes.getBoundHeight() + 0.3f);
			Vector2 p2 = new Vector2(boxes.getBoundX() + boxes.getBoundWidth(), boxes.getBoundY() + boxes.getBoundHeight() + 0.3f);

			Info.drawLine(p1, p2, 0.02f);

			if (intersectSegmentPolygon(p1, p2, cockroach.getBound())) {
				cockroach.setLimit(boxes.getBoundY() + boxes.getBoundHeight() + cockroach.getBoundHeight() / 2, platform.getUpperLimit() - cockroach.getBoundHeight() / 2);
				return;
			}
		}
		// set limit to terrain
		cockroach.setLimit(platform.getLowerLimit() + cockroach.getBoundHeight() / 2, platform.getUpperLimit() - cockroach.getBoundHeight() / 2);
	}

	private boolean activeWall = true;

	private void responseWall() {
		if (isOverlapWall() && activeWall) {
			cockroach.eventSpeak();
			ui.getLive().decrease(1);
			// sound effect
			R.assets.soundWav("hit").play();
			activeWall = false;
		}
	}

	private boolean isOverlapWall() {
		for (Wall wall : environment.getWalls()) {
			if (overlapConvexPolygons(cockroach.getBound(), wall.getBound())) {
				return true;
			}
		}
		activeWall = true;
		return false;
	}

	private boolean activeSlipper = true;

	private void responseSlipper() {
		if (isOverlapSlipper() && activeSlipper) {
			cockroach.eventSpeak();
			ui.getLive().decrease(1);
			// sound effect
			R.assets.soundWav("hit").play();
			activeSlipper = false;
		}
	}

	private boolean isOverlapSlipper() {
		for (Slipper slipper : object.getSlippers()) {
			if (overlapConvexPolygons(cockroach.getBound(), slipper.getBound())) {
				return true;
			}
		}
		activeSlipper = true;
		return false;
	}

	private boolean activeWindmill = true;

	private void responseWindmill() {
		if (isOverlapWindmill() && activeWindmill) {
			cockroach.eventSpeak();
			ui.getLive().decrease(1);
			// sound effect
			R.assets.soundWav("hit").play();
			activeWindmill = false;
		}
	}

	private boolean isOverlapWindmill() {
		for (Windmill windmill : environment.getWindmills()) {
			if (overlapConvexPolygons(cockroach.getBound(), windmill.getBoundA()) || overlapConvexPolygons(cockroach.getBound(), windmill.getBoundB())) {
				return true;
			}
		}
		activeWindmill = true;
		return false;
	}

	private boolean activeCone = true;

	private void responseCone() {
		if (isOverlapCone() && activeCone) {
			cockroach.eventSpeak();
			ui.getLive().decrease(1);
			// sound effect
			R.assets.soundWav("hit").play();
			activeCone = false;
		}
	}

	private boolean isOverlapCone() {
		for (Cones cones : environment.getCones()) {
			if (overlapConvexPolygons(cockroach.getBound(), cones.getBound())) return true;
		}
		activeCone = true;
		return false;
	}

	private void responseHeart() {
		if (isOverlapHeart()) {
			ui.getLive().increase(1);

			R.assets.soundWav("power-up").play();
		}
	}

	private boolean isOverlapHeart() {
		for (Heart heart : environment.getHearts()) {
			if (overlapConvexPolygons(cockroach.getBound(), heart.getBound())) {
				environment.removeHeart(heart);
				return true;
			}
		}
		return false;
	}

	private void responseBattery() {
		for (Battery battery : environment.getBatteries()) {
			if (overlapConvexPolygons(cockroach.getBound(), battery.getBound())) {
				Model model = (Model) battery.getModel();
				if (model == Model.SINGLE) {
					cockroach.getEnergy().setHalfMAX();
				}
				if (model == Model.DOUBLE) {
					cockroach.getEnergy().setMAX();
				}
				R.assets.soundWav("power-up").play();

				// remove battery
				environment.removeBattery(battery);
			}
		}
	}

	private void responseCoin() {
		if (isOverlapCoin()) {
			ui.getScore().increase();

			R.assets.soundWav("collect-coin").play();
		}
	}

	private boolean isOverlapCoin() {
		for (Coin coin : environment.getCoin()) {
			if (overlapConvexPolygons(cockroach.getBound(), coin.getBound())) {
				environment.removeCoin(coin);
				return true;
			}
		}
		return false;
	}

}
