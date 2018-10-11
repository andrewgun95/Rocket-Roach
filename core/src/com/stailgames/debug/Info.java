package com.stailgames.debug;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.stailgames.main.World;

/**
 * @author Andreas
 *
 */
public class Info {

	private static final Color COLOR = Color.RED;

	// font debug parameters
	private static OrthographicCamera fontCamera;
	private static BitmapFont font;
	private static SpriteBatch fontBatch;

	// shape debug parameters
	private static OrthographicCamera renderCamera;
	private static ShapeRenderer shapeRender;

	private static boolean visible = false;

	static {
		// initialization font debug
		fontCamera = new OrthographicCamera(World.WIDTH * World.PIXEL_PER_METER, World.HEIGHT * World.PIXEL_PER_METER);
		fontCamera.position.set(fontCamera.viewportWidth / 2, fontCamera.viewportHeight / 2, 0);
		fontCamera.setToOrtho(true);
		fontCamera.update();
		font = new BitmapFont();
		fontBatch = new SpriteBatch();
		// initialization camera debug
		renderCamera = new OrthographicCamera(World.WIDTH, World.HEIGHT);
		renderCamera.position.set(renderCamera.viewportWidth / 2, renderCamera.viewportHeight / 2, 0);
		renderCamera.update();
		shapeRender = new ShapeRenderer();
		shapeRender.setAutoShapeType(true);
		// initialization physics debug
	}

	private static Array<Text> texts = new Array<Text>();
	private static Array<Line> lines = new Array<Line>();
	private static Array<Shape> shapes = new Array<Shape>();

	public static void setVisible(boolean visible) {
		Info.visible = visible;
	}

	public static boolean getVisible() {
		return visible;
	}

	public static void render() {
		if (!visible) return;

		shapeRender.setProjectionMatrix(renderCamera.combined);
		shapeRender.begin();

		// DRAW LINE
		for (Line line : lines) {
			shapeRender.set(ShapeType.Filled);
			shapeRender.setColor(line.color);
			shapeRender.rectLine(line.p1, line.p2, line.width);
		}
		lines.clear();

		// DRAW SHAPE
		for (Shape shape : shapes) {
			shapeRender.set(ShapeType.Line);
			shapeRender.setColor(shape.color);
			shapeRender.polygon(shape.polygon.getTransformedVertices());
		}
		shapes.clear();

		shapeRender.end();

		// DRAW FONT
		fontBatch.setProjectionMatrix(fontCamera.combined);
		fontBatch.begin();
		font.getData().setScale(1.0f, -1.0f);
		for (Text text : texts) {
			font.setColor(text.color);
			font.draw(fontBatch, text.sequence, text.x, text.y);
		}
		texts.clear();
		fontBatch.end();
	}

	public static void render(Batch batch) {
		if (!visible) return;

		shapeRender.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRender.setTransformMatrix(batch.getTransformMatrix());
		shapeRender.begin();

		// DRAW LINE
		for (Line line : lines) {
			shapeRender.set(ShapeType.Filled);
			shapeRender.setColor(line.color);
			shapeRender.rectLine(line.p1, line.p2, line.width);
		}
		lines.clear();

		// DRAW SHAPE
		for (Shape shape : shapes) {
			shapeRender.set(ShapeType.Line);
			shapeRender.setColor(shape.color);
			shapeRender.polygon(shape.polygon.getTransformedVertices());
		}
		shapes.clear();

		shapeRender.end();

		// DRAW FONT
		fontBatch.setProjectionMatrix(fontCamera.combined);
		fontBatch.begin();
		font.getData().setScale(1.0f, -1.0f);
		for (Text text : texts) {
			font.setColor(text.color);
			font.draw(fontBatch, text.sequence, text.x, text.y);
		}
		texts.clear();
		fontBatch.end();
	}

	/**
	 * Draw string based of pixel coordinate (World.WIDTH * World.PIXEL_PER_METER, World.HEIGHT * World.PIXEL_PER_METER)
	 * 
	 * @param sequence
	 * @param x
	 * @param y
	 */
	public static void drawString(CharSequence sequence, float x, float y) {
		drawString(sequence, x, y, COLOR);
	}

	/**
	 * Draw string based of pixel coordinate (World.WIDTH * World.PIXEL_PER_METER, World.HEIGHT * World.PIXEL_PER_METER)
	 * 
	 * @param sequence
	 * @param x
	 * @param y
	 * @param color
	 */
	public static void drawString(CharSequence sequence, float x, float y, Color color) {
		Text text = new Text();
		text.sequence = sequence;
		text.x = x;
		text.y = y;
		text.color = color;
		texts.add(text);
	}

	private static class Text {
		CharSequence sequence = null;
		float x = .0f, y = .0f;
		Color color = null;
	}

	/**
	 * Draw line based of world coordinate (World.WIDTH , World.HEIGHT)
	 * 
	 * @param p1
	 * @param p2
	 */
	public static void drawLine(Vector2 p1, Vector2 p2) {
		drawLine(p1, p2, 0.01f, COLOR);
	}

	/**
	 * Draw line based of world coordinate (World.WIDTH , World.HEIGHT)
	 * 
	 * @param p1
	 * @param p2
	 * @param width
	 */
	public static void drawLine(Vector2 p1, Vector2 p2, float width) {
		drawLine(p1, p2, width, COLOR);
	}

	/**
	 * Draw line based of world coordinate (World.WIDTH , World.HEIGHT)
	 * 
	 * @param p1
	 * @param p2
	 * @param color
	 */
	public static void drawLine(Vector2 p1, Vector2 p2, float width, Color color) {
		Line line = new Line();
		line.p1 = p1;
		line.p2 = p2;
		line.width = width;
		line.color = color;
		lines.add(line);
	}

	private static class Line {
		Vector2 p1 = null, p2 = null;
		float width = 0.0f;
		Color color = null;
	}

	/**
	 * Draw shape based of world coordinate (World.WIDTH , World.HEIGHT)
	 * 
	 * @param polygon
	 */
	public static void drawShape(Polygon polygon) {
		drawShape(polygon, COLOR);
	}

	/**
	 * Draw shape based of world coordinate (World.WIDTH , World.HEIGHT)
	 * 
	 * @param polygon
	 * @param color
	 */
	public static void drawShape(Polygon polygon, Color color) {
		Shape shape = new Shape();
		shape.polygon = polygon;
		shape.color = color;
		shapes.add(shape);
	}

	public static class Shape {
		Polygon polygon = null;
		Color color = null;
	}

	public static void drawCamera(Batch batch, Camera camera) {
		shapeRender.setProjectionMatrix(batch.getProjectionMatrix());
		shapeRender.setTransformMatrix(batch.getTransformMatrix());
		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(COLOR);
		shapeRender.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
		shapeRender.end();
	}

	public static void drawCamera(Stage stage) {
		shapeRender.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
		shapeRender.setTransformMatrix(stage.getBatch().getTransformMatrix());
		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(stage.getDebugColor());
		shapeRender.rect(0, 0, stage.getWidth(), stage.getHeight());
		shapeRender.end();
	}
}
