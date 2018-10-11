package com.stailgames.gameobject;

import static com.stailgames.utils.Converter.pixelToMeter;
import static com.stailgames.utils.Randomize.random;
import static com.stailgames.utils.Randomize.randomOdd;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.stailgames.components.Battery;
import com.stailgames.components.Battery.Model;
import com.stailgames.components.Boxes;
import com.stailgames.components.Coins;
import com.stailgames.components.Coins.Coin;
import com.stailgames.components.Coins.Pattern;
import com.stailgames.components.Component;
import com.stailgames.components.Cones;
import com.stailgames.components.Heart;
import com.stailgames.components.Walls;
import com.stailgames.components.Walls.Wall;
import com.stailgames.main.World;
import com.stailgames.components.Windmill;

public class Environment extends Component {

	public static final float CLEARANCE = pixelToMeter(600);

	private Array<Component> obstacles = new Array<Component>();
	private Array<Component> items = new Array<Component>();

	private boolean active;
	private float pointer;

	public Environment() {
		super("Obstacle");
		active = true;
		pointer = World.WIDTH + 0.2f;
	}

	public void activated(boolean state) {
		active = state;
	}

	public void reset() {
		active = true;
		pointer = World.WIDTH + 0.2f;
	}

	public boolean isActive() {
		return active;
	}

	@Override
	public void update(float delta) {
		create();
		// update obstacle
		for (Component obstacle : obstacles) {
			obstacle.update(delta);
		}
		// update item
		for (Component item : items) {
			item.update(delta);
		}
		destroy();
	}

	private void create() {
		// state for created terrain
		if (!active) return;

		if (obstacles.size == 0) {
			createWall(World.WIDTH + 0.2f, false);
		} else {
			if (pointer < World.WIDTH - CLEARANCE) {

				// randomize wall set into motion for chance 30 percent will appear
				if (random(30)) {
					createWall(World.WIDTH + 0.2f, true);
				} else {

					// center position between top wall and bottom wall
					float wallPosition = createWall(World.WIDTH + 0.2f, false);

					centerWall: {
						// randomize power up for chance 70 percent will appear
						if (random(70)) {

							// randomize power up for chance 70 percent battery will appear
							if (random(70)) {

								// randomize battery for chance 70 percent single battery will appear
								if (random(70)) {
									createBattery(World.WIDTH + 0.2f, wallPosition + 0.02f, Model.SINGLE);
								} else
									createBattery(World.WIDTH + 0.2f, wallPosition + 0.02f, Model.DOUBLE);

							} else {
								createHeart(World.WIDTH + 0.2f, wallPosition + 0.02f);
							}

						}
						break centerWall;
					}

					betweenWall: {
						// randomize 2nd obstacle for chance 80 percent will appear
						if (random(80)) {

							if (random(50)) {

								float boxPosition = random(World.HEIGHT / 2, World.HEIGHT / 3, (World.HEIGHT * 2) / 3);
								boxPosition = boxPosition - 0.5f;
								createBoxes(World.WIDTH + 0.2f + CLEARANCE / 2, boxPosition, randomOdd(1, 3));

								// randomize coin for chance 80 percent will appear
								if (random(60)) {
									createCoin(World.WIDTH + 0.2f + CLEARANCE / 2, boxPosition + 0.8f, randomOdd(1, 3), Pattern.CURVE);
								} else
									createHeart(World.WIDTH + 0.2f + CLEARANCE / 2, boxPosition + 0.5f);
							} else {
								// randomize cones for chance 90 percent will appear
								if (random(90)) {
									createCones(World.WIDTH + 0.2f + CLEARANCE / 2, 0.1f, random(0, 5));
								} else
									createWindmill(World.WIDTH + 0.2f + CLEARANCE / 2, 1.1f);
							}
						}

						break betweenWall;
					}

				}

				pointer = World.WIDTH + 0.2f;
			}
			pointer += Movement.getSpeed().game;
		}
	}

	private float createWall(float x, boolean motion) {
		Walls wall = Pools.get(Walls.class).obtain();
		float y = Walls.POSITION.random().getValue();
		wall.create(x, y);
		if (motion) wall.setMotion(Walls.POSITION.BOTTOM.getValue(), Walls.POSITION.UPPER.getValue(), Movement.getSpeed().wallmotion);
		obstacles.add(wall);
		return y;
	}

	private void createCones(float x, float y, int unit) {
		Cones cones = Pools.get(Cones.class).obtain();
		cones.create(x, y, unit);
		obstacles.add(cones);
	}

	private void createBoxes(float x, float y, int unit) {
		Boxes boxes = Pools.get(Boxes.class).obtain();
		boxes.create(x, y, unit);
		obstacles.add(boxes);
	}

	private void createWindmill(float x, float y) {
		Windmill windmill = Pools.get(Windmill.class).obtain();
		windmill.create(x, y, 2.5f);
		obstacles.add(windmill);
	}

	private void createCoin(float x, float y, int unit, Pattern pattern) {
		Coins coin = Pools.get(Coins.class).obtain();
		coin.create(x, y, unit, 0.1f, pattern);
		items.add(coin);
	}

	private void createBattery(float x, float y, Model model) {
		Battery battery = Pools.get(Battery.class).obtain();
		battery.create(x, y, model);
		items.add(battery);
	}

	private void createHeart(float x, float y) {
		Heart heart = Pools.get(Heart.class).obtain();
		heart.create(x, y, 0.1f);
		items.add(heart);
	}

	public void removeCoin(Coin coin) {
		for (Component component : items) {
			if (component instanceof Coins) {
				Coins coins = (Coins) component;
				coins.get().removeValue(coin, false);
			}
		}
	}

	public void removeBattery(Battery battery) {
		items.removeValue(battery, false);
	}

	public void removeHeart(Heart heart) {
		items.removeValue(heart, false);
	}

	private void destroy() {
		if (obstacles.size == 0) return;
		// destroy obstacle
		Component firstObstacle = obstacles.first();
		if (firstObstacle.getBoundX() + firstObstacle.getBoundWidth() < 0) {
			obstacles.removeValue(firstObstacle, false);
			Pools.free(firstObstacle);
		}

		if (items.size == 0) return;
		// destroy item
		Component firstItem = items.first();
		if (firstItem.getBoundX() + firstItem.getBoundWidth() < 0) {
			items.removeValue(firstItem, false);
			// special case when first item is coins need to destroy each coin
			if (firstItem instanceof Coins) {
				Coins coins = (Coins) firstItem;
				coins.get().clear();
			}
			Pools.free(firstItem);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (Component obstalce : obstacles) {
			obstalce.draw(batch, parentAlpha);
		}
		for (Component item : items) {
			item.draw(batch, parentAlpha);
		}
	}

	@Override
	public void drawDebug(ShapeRenderer shapes) {
		if (!getDebug()) return;

		shapes.set(ShapeType.Line);
		shapes.setColor(getStage().getDebugColor());
		for (Wall wall : getWalls()) {
			shapes.polygon(wall.getBound().getTransformedVertices());
		}
		for (Cones cones : getCones()) {
			shapes.polygon(cones.getBound().getTransformedVertices());
		}
		for (Boxes boxes : getBoxes()) {
			shapes.polygon(boxes.getBound().getTransformedVertices());
		}
		for (Windmill windmill : getWindmills()) {
			shapes.polygon(windmill.getBoundA().getTransformedVertices());
			shapes.polygon(windmill.getBoundB().getTransformedVertices());
		}
		for (Coin coin : getCoin()) {
			shapes.polygon(coin.getBound().getTransformedVertices());
		}
		for (Battery battery : getBatteries()) {
			shapes.polygon(battery.getBound().getTransformedVertices());
		}
		for (Heart heart : getHearts()) {
			shapes.polygon(heart.getBound().getTransformedVertices());
		}
		shapes.setColor(Color.BROWN);
		shapes.line(pointer, 0, pointer, World.HEIGHT);
	}

	public Array<Wall> getWalls() {
		Array<Wall> walls = new Array<Wall>();
		for (Component component : obstacles) {
			if (component instanceof Walls) walls.addAll(((Walls) component).get());
		}
		return walls;
	}

	public Array<Cones> getCones() {
		Array<Cones> cones = new Array<Cones>();
		for (Component component : obstacles) {
			if (component instanceof Cones) cones.add((Cones) component);
		}
		return cones;
	}

	public Array<Windmill> getWindmills() {
		Array<Windmill> windmills = new Array<Windmill>();
		for (Component component : obstacles) {
			if (component instanceof Windmill) windmills.add((Windmill) component);
		}
		return windmills;
	}

	public Array<Boxes> getBoxes() {
		Array<Boxes> boxes = new Array<Boxes>();
		for (Component component : obstacles) {
			if (component instanceof Boxes) boxes.add((Boxes) component);
		}
		return boxes;
	}

	public Array<Coin> getCoin() {
		Array<Coin> coins = new Array<Coin>();
		for (Component component : items) {
			if (component instanceof Coins) coins.addAll(((Coins) component).get());
		}
		return coins;
	}

	public Array<Coins> getCoins() {
		Array<Coins> coins = new Array<Coins>();
		for (Component component : items) {
			if (component instanceof Coins) coins.add(((Coins) component));
		}
		return coins;
	}

	public Array<Battery> getBatteries() {
		Array<Battery> batteries = new Array<Battery>();
		for (Component component : items) {
			if (component instanceof Battery) batteries.add((Battery) component);
		}
		return batteries;
	}

	public Array<Heart> getHearts() {
		Array<Heart> hearts = new Array<Heart>();
		for (Component component : items) {
			if (component instanceof Heart) hearts.add((Heart) component);
		}
		return hearts;
	}

	public boolean isClear(){
		return obstacles.size == 0;
	}
	
}
