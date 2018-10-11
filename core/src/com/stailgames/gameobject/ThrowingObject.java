package com.stailgames.gameobject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.stailgames.components.Component;
import com.stailgames.components.Slipper;
import com.stailgames.components.Warning;
import com.stailgames.main.World;

public class ThrowingObject extends Component {

	private Array<Component> objects = new Array<Component>();

	private Component target;
	private float delay;

	private Warning warning;
	private boolean active;

	public ThrowingObject(float delay, Component target) {
		super("Throwing Object");
		this.delay = delay;
		this.target = target;

		warning = new Warning();
		active = true;
	}

	private float timer = 0.0f;
	private float targetPosition = 0.0f;

	public void activated(boolean state) {
		active = state;
	}
	
	public void reset() {
		active = true;
	}

	@Override
	public void update(float delta) {
		if (active){
			// create
			if ((timer += delta) > delay) {
				targetPosition = target.getBoundY() + target.getBoundHeight() / 2;
				warning.create(World.WIDTH - 0.2f, targetPosition, 2f);
				timer = 0.0f;
			}

			if (warning.isOver() && targetPosition != 0.0f) {
				Slipper slipper = Pools.get(Slipper.class).obtain();
				slipper.create(World.WIDTH + 0.5f, targetPosition, 2f);
				objects.add(slipper);

				targetPosition = 0.0f;
			}
		}		

		// update
		for (Component component : objects) {
			component.update(delta);
		}
		warning.update(delta);

		// destroy
		destroy();
	}

	private void destroy() {
		if (objects.size == 0) return;
		// destroy object
		Component firstObject = objects.first();
		if (firstObject.getBoundX() + firstObject.getBoundWidth() < 0) {
			objects.removeValue(firstObject, false);
			Pools.free(firstObject);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (Component component : objects) {
			component.draw(batch, parentAlpha);
		}
		warning.draw(batch, parentAlpha);
	}

	@Override
	public void drawDebug(ShapeRenderer shapes) {
		if (!getDebug()) return;
		shapes.set(ShapeType.Line);
		shapes.setColor(getStage().getDebugColor());
		for (Component component : objects) {
			shapes.polygon(component.getBound().getTransformedVertices());
		}
	}

	public Array<Slipper> getSlippers() {
		Array<Slipper> slippers = new Array<Slipper>();
		for (Component component : objects) {
			if (component instanceof Slipper) slippers.add((Slipper) component);
		}
		return slippers;
	}

}
