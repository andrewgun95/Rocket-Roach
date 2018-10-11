package com.stailgames.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.stailgames.main.World;

public class CameraHelper {

	private static final float Z_IN = 0.02f;
	private static final float Z_OUT = 10f;
	private static final float SPEED = 0.02f;

	// CONTROLLER
	// UP : W
	// DOWN : S
	// LEFT : A
	// RIGHT : D
	// ZOOM_IN : Z
	// ZOOM_OUT : X
	// RESET : C

	public static void update(OrthographicCamera camera) {
		camera.position.y = Gdx.input.isKeyPressed(Keys.W) ? camera.position.y + SPEED : camera.position.y;
		camera.position.y = Gdx.input.isKeyPressed(Keys.S) ? camera.position.y - SPEED : camera.position.y;
		camera.position.x = Gdx.input.isKeyPressed(Keys.D) ? camera.position.x + SPEED : camera.position.x;
		camera.position.x = Gdx.input.isKeyPressed(Keys.A) ? camera.position.x - SPEED : camera.position.x;
		camera.zoom = Gdx.input.isKeyPressed(Keys.Z) ? MathUtils.clamp(camera.zoom - SPEED, Z_IN, Z_OUT) : camera.zoom;
		camera.zoom = Gdx.input.isKeyPressed(Keys.X) ? MathUtils.clamp(camera.zoom + SPEED, Z_IN, Z_OUT) : camera.zoom;
		if (Gdx.input.isKeyPressed(Keys.C)) {
			camera.position.set(World.WIDTH / 2, World.HEIGHT / 2, 0);
			camera.zoom = 1f;
		}
		camera.update();
	}

}
