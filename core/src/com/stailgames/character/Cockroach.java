package com.stailgames.character;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.stailgames.components.Component;
import com.stailgames.gameobject.Movement;
import com.stailgames.main.R;
import com.stailgames.main.World;

public class Cockroach extends Component {

	// private static final float WIDTH = pixelToMeter(101);
	// private static final float HEIGHT = pixelToMeter(116);
	private static final float WIDTH = pixelToMeter(70);
	private static final float HEIGHT = pixelToMeter(90);
	private static final float POWER = 3f;

	private float y;
	private boolean active;

	// event parameter
	private boolean boost;

	// physics parameter
	private float acceleration, velocity;

	// limit parameter
	private float minY;
	private float maxY;

	// feature parameter
	private Energy energy;
	private Fire fire;
	private Smoke smoke;
	private Array<Chat> chats;

	public Cockroach(float x, float y) {
		super("Cockroach");
		this.y = y;
		active = true;
		setPosition(x, y);
		init();
	}

	private void init() {
		// event
		boost = false;
		// physics
		velocity = 0.0f;
		acceleration = 0.0f;
		// limit
		minY = 0;
		maxY = World.HEIGHT;
		// feature
		fire = new Fire();
		energy = new Energy();
		chats = new Array<Chat>();
		smoke = new Smoke();

		setBoundingRectangle(getX() - ((WIDTH - 0.2f) / 2), getY() - ((HEIGHT - 0.2f) / 2), (WIDTH - 0.2f), (HEIGHT - 0.2f));
	}

	public void activated(boolean state) {
		active = state;
	}

	@Override
	public void update(float delta) {

		physicsUpdate(delta);
		handleEvent(delta);

		setBoundingRectangle(getX() - ((WIDTH - 0.2f) / 2), getY() - ((HEIGHT - 0.2f) / 2), (WIDTH - 0.2f), (HEIGHT - 0.2f));
	}

	private void physicsUpdate(float delta) {
		// physics update : acceleration by gravity
		acceleration = y == minY ? 0.0f : World.getGravity();
		velocity = MathUtils.clamp(velocity + (acceleration * delta), -0.1f, 0.1f);
		y += velocity;
		// limit
		y = MathUtils.clamp(y, minY, maxY);

		setPosition(getX(), y);
	}

	private void handleEvent(float delta) {
		// handle event
		if (active) {
			event: {
				if (boost) {
					if (energy.isEmpty()) {
						fire.active(false);
						break event;
					} else {
						energy.decrease(POWER);
						fire.active(true);
					}
					// action give -> momentum : p = m.v (m = 1)
					velocity = 0.02f;
				} else {
					fire.active(false);
				}
			}

			// update effect in specific position
			effect: {
				// character - 2
				// fire.set(getX() - 0.35f, getY() - 0.7f);
				// character - 1
				fire.set(getX() - 0.3f, getY() - 0.6f);
				fire.update(delta);
				break effect;
			}

			// update charge by increasing when cockroach on the top of platform
			charge: {
				if (y == minY) {
					energy.increase(Movement.getSpeed().charging);
				}
				break charge;
			}
		}

		// update chat
		chat: {
			for (Chat chat : chats) {
				chat.update(delta);
				if (chat.isOver()) {
					chats.removeValue(chat, false);
				}
			}
			break chat;
		}

		smoke: {
			smoke.set(getX() - 0.3f, getY() - 0.2f, 0.1f);
			smoke.update(delta);
			break smoke;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (active) fire.draw(batch);

		draw(batch, R.assets.image("character/cockroach"), getX(), getY(), WIDTH, HEIGHT);
		if (active) {
			energy.draw(batch, getX() - 0.1f, getY() + 0.4f);
		}
		for (Chat chat : chats) {
			chat.draw(batch, parentAlpha);
		}

		smoke.draw(batch, parentAlpha);
	}

	private void draw(Batch batch, TextureRegion reg, float x, float y, float width, float height) {
		batch.draw(reg, x - width / 2, y - height / 2, width, height);
	}

	public void eventSpeak() {
		Chat chat = Pools.get(Chat.class).obtain();
		chat.create(getX() + 0.1f, getY(), 1.0f);
		chats.add(chat);
	}

	public void eventBoost(boolean state) {
		boost = state;
		if (state)
			R.assets.soundMp3("boost").play(1.0f);
		else
			R.assets.soundMp3("boost").stop();
	}

	public void eventSmoke(boolean state) {
		smoke.active(state);
	}

	public void setLimit(float minY, float maxY) {
		this.minY = minY;
		this.maxY = maxY;
	}

	public Energy getEnergy() {
		return energy;
	}

}
