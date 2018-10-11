package com.stailgames.components;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.gameobject.Movement;
import com.stailgames.main.R;

public class Windmill extends Component implements Poolable {

	// utilities
	private static void drawCenter(Batch batch, TextureRegion region, float x, float y, float width, float height, float rotation) {
		batch.draw(region, x - width / 2, y - height / 2, width / 2, height / 2, width, height, 1f, 1f, rotation);
	}

	private static final float WIDTH = pixelToMeter(265);
	private static final float HEIGHT = pixelToMeter(265);
	private static final float BGWIDTH = pixelToMeter(154);
	private static final float BGHEIGHT = pixelToMeter(225);
	
	public float x, y;
	public float rotation;

	private Polygon boundA;
	private Polygon boundB;

	public Windmill() {
		super("Windmill");
		x = 0.0f;
		y = 0.0f;
		rotation = 0.0f;
	}

	public void create(float x, float y, float rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;

		// bound for detector
		boundA = new Polygon(new float[] { 
				x - 0.12f, y - HEIGHT / 2, 
				x - 0.12f, y + HEIGHT / 2, 
				x + 0.12f, y + HEIGHT / 2, 
				x + 0.12f, y - HEIGHT / 2 });
		
		boundB = new Polygon(new float[] { 
				x - WIDTH / 2, y - 0.12f, 
				x - WIDTH / 2, y + 0.12f, 
				x + WIDTH / 2, y + 0.12f, 
				x + WIDTH / 2, y - 0.12f });
		
		// combine bound A and bound B
		setBoundingRectangle(x - WIDTH/2,y - HEIGHT/2, WIDTH, HEIGHT);
	}

	@Override
	public void update(float delta) {
		x += Movement.getSpeed().game;

		angle += rotation;

		boundA.setVertices(new float[] { 
				x - 0.12f, y - HEIGHT / 2, 
				x - 0.12f, y + HEIGHT / 2, 
				x + 0.12f, y + HEIGHT / 2, 
				x + 0.12f, y - HEIGHT / 2 });
		boundA.setRotation(angle);
		boundA.setOrigin(x, y);
		
		boundB.setVertices(new float[] { 
				x - WIDTH / 2, y - 0.12f, 
				x - WIDTH / 2, y + 0.12f, 
				x + WIDTH / 2, y + 0.12f, 
				x + WIDTH / 2, y - 0.12f });
		boundB.setRotation(angle);
		boundB.setOrigin(x, y);
		
		setBoundingRectangle(x - WIDTH/2, y - HEIGHT/2, WIDTH, HEIGHT);
	}

	private float angle = 0.0f;

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(R.assets.image("component/windmill-house"), x - 0.5f, y - 1.0f, BGWIDTH, BGHEIGHT);
		drawCenter(batch, R.assets.image("component/windmill"), x, y, WIDTH, HEIGHT, angle);
	}

	@Override
	public void drawDebug(ShapeRenderer shapes) {
		if (!getDebug()) return;
		shapes.set(ShapeType.Line);
		shapes.setColor(getStage().getDebugColor());
		shapes.polygon(boundA.getTransformedVertices());
		shapes.polygon(boundB.getTransformedVertices());
	}

	@Override
	public void reset() {
		x = 0.0f;
		y = 0.0f;
		rotation = 0.0f;
	}

	public Polygon getBoundA() {
		return boundA;
	}

	public Polygon getBoundB() {
		return boundB;
	}
}
