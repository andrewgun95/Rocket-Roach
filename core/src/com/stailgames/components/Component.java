package com.stailgames.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class Component extends Actor {

	private boolean enable;
	private Polygon bound;

	protected Component() {
		this(null);
	}

	protected Component(String name) {
		setName(name);
		bound = new Polygon();
		enable = true;
	}

	@Override
	abstract public void draw(Batch batch, float parentAlpha);

	@Override
	public void act(float delta) {
		super.act(delta);
		if (enable) update(delta);
	}

	public void update(float delta) {
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setBoundRotation(float degrees) {
		bound.setRotation(degrees);
	}

	public void setBounding(float[] vertices) {
		bound.setVertices(vertices);
	}

	public void setBounding(float[] vertices, float originX, float originY) {
		bound.setVertices(vertices);
		bound.setOrigin(originX, originY);
	}

	public void setBoundingRectangle(float x, float y, float width, float height) {
		bound.setVertices(new float[] { x, y, x, y + height, x + width, y + height, x + width, y });
		bound.setOrigin(x + width / 2, y + height / 2);
	}

	public Polygon getBound() {
		return bound;
	}

	public float getBoundX() {
		return bound.getBoundingRectangle().x;
	}

	public float getBoundY() {
		return bound.getBoundingRectangle().y;
	}

	public float getBoundWidth() {
		return bound.getBoundingRectangle().width;
	}

	public float getBoundHeight() {
		return bound.getBoundingRectangle().height;
	}

	@Override
	public void drawDebug(ShapeRenderer shapes) {
		if (bound.area() == 0) {
			drawDebugBounds(shapes);
		} else {
			if (!getDebug()) return;
			shapes.set(ShapeType.Line);
			shapes.setColor(getStage().getDebugColor());
			shapes.polygon(bound.getTransformedVertices());
		}
	}

}