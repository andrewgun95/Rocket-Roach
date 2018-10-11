package com.stailgames.components;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.main.R;

public class Warning extends Component implements Poolable {

	private final float WIDTH = pixelToMeter(57);
	private final float HEIGHT = pixelToMeter(50);

	public float delay, alpha;
	public Sprite sprite;

	private boolean play;

	public Warning() {
		super("Warning");
		delay = 0.0f;
		alpha = 0.0f;
		play = false;
		sprite = new Sprite(R.assets.image("component/warning"));
	}

	public void create(float x, float y, float duration) {
		timer = 0.0f;
		play = true;
		delay = duration;
		sprite.setPosition(x - WIDTH / 2, y - HEIGHT / 2);
	}

	private float timer = 0.0f;

	@Override
	public void update(float delta) {
		if ((timer += delta) > delay) {
			alpha = 0.0f;
			play = false;
			timer = 0.0f;
		}
		if (play) alpha = alpha < 1.0f ? alpha + 0.05f : 0.0f;
	}

	public boolean isOver() {
		return !play;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setSize(WIDTH, HEIGHT);
		sprite.setAlpha(alpha);
		sprite.draw(batch);
	}

	@Override
	public void reset() {
		delay = 0.0f;
		alpha = 0.0f;
		play = false;
	}

}