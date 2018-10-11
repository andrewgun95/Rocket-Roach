package com.stailgames.character;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.stailgames.main.R;

public class Fire extends Sprite {

	private static final float WIDTH = pixelToMeter(60);
	private static final float HEIGHT = pixelToMeter(60);

	private Animation animation;
	private float frame;

	private boolean active;

	public Fire() {
		animation = new Animation(0.1f, R.assets.image("character/fire-effect").split(60, 60)[0]);
		frame = 0.0f;
		active = false;
	}

	public void set(float x, float y) {
		setPosition(x, y);
		setSize(WIDTH, HEIGHT);
	}

	public void active(boolean state) {
		active = state;
	}

	public void update(float delta) {
		setRegion(animation.getKeyFrame((frame += delta), true));
		setSize(WIDTH, HEIGHT);
	}

	@Override
	public void draw(Batch batch) {
		if (active) super.draw(batch);
	}

}
