package com.stailgames.gameobject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.stailgames.components.Component;
import com.stailgames.components.Road;
import com.stailgames.main.World;

public class Platform extends Component {

	private Pool<Road> poolRoad = new Pool<Road>() {

		@Override
		protected Road newObject() {
			return new Road();
		}
	};

	private Array<Component> platform = new Array<Component>();

	private float elevation;

	public Platform(float elevation) {
		super("Platform");
		this.elevation = elevation;
		// default
		float x = 0.0f;
		Road road = null;
		do {
			// initial platform
			road = poolRoad.obtain();
			road.create(x, elevation);
			platform.add(road);

			x += road.getBoundWidth();

		} while (x < World.WIDTH);
	}

	public void update(float delta) {
		create();
		// update platform
		for (Component component : platform) {
			component.act(delta);
		}
		destroy();
	}

	private void create() {
		Road road = null;
		// create platform
		Component component = platform.peek();
		if (component.getBoundX() + component.getBoundWidth() < World.WIDTH) {
			road = poolRoad.obtain();
			road.create(component.getBoundX() + component.getBoundWidth(), elevation);
			platform.add(road);

		}
	}

	private void destroy() {
		// destroy platform
		Component component = platform.first();
		if (component.getBoundX() + component.getBoundWidth() < 0) {
			platform.removeValue(component, false);
			Pools.free(component);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (Component component : platform) {
			component.draw(batch, parentAlpha);
		}
	}

	@Override
	public void drawDebug(ShapeRenderer shapes) {
		if (!getDebug()) return;
		shapes.set(ShapeType.Line);
		shapes.setColor(getStage().getDebugColor());
		for (Component component : platform) {
			shapes.polygon(((Component) component).getBound().getTransformedVertices());
		}
	}

	public float getLowerLimit() {
		return 0.15f;
	}

	public float getUpperLimit() {
		return World.HEIGHT;
	}
}
