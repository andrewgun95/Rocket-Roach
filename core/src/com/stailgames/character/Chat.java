package com.stailgames.character;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.components.Component;
import com.stailgames.main.R;
import com.stailgames.utils.Randomize;

public class Chat extends Component implements Poolable {

	private static final float WIDTH = pixelToMeter(77);
	private static final float HEIGHT = pixelToMeter(57);
	
	private float duration;

	private Sprite sprite;
	private boolean visible;
	private float scale;

	public Chat() {
		sprite = new Sprite();
	}

	public void create(float x, float y, float duration) {
		sprite.setRegion(R.assets.image("character/chat-1"));
		setPosition(x + Randomize.random(-0.1f, 0.1f), y + Randomize.random(-0.1f, 0.1f));
		this.duration = duration;
		visible = true;
		scale = 0.0f;
	}

	private float tick = 0.0f;

	public void update(float delta) {
		if ((tick += delta) > duration) {
			visible = false;
			tick = 0.0f;
		}

		scale = scale < 1.0f ? scale + 0.1f : 1.0f;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setPosition(getX(), getY());
		sprite.setSize(WIDTH * scale, HEIGHT * scale);
		if (visible) sprite.draw(batch);
	}

	public boolean isOver() {
		return !visible;
	}

	@Override
	public void reset() {
	}

	@Override
	public boolean equals(Object chat) {
		return getX() == ((Chat) chat).getX() && getY() == ((Chat) chat).getY();
	}

}
