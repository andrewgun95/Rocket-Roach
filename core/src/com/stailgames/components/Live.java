package com.stailgames.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.stailgames.main.R;

/**
 * this component in pixel, not in meter
 *
 */
public class Live extends Component {

	public static final int MAX_LIVE = 5;

	public float x, y;
	public int live;
	public float offset;

	public TextureRegion image;
	public float imageWidth;
	public float imageHeight;

	public Live(float x, float y, float offset) {
		this.x = x;
		this.y = y;
		this.offset = offset;

		image = R.assets.image("ui/heart");
		imageWidth = image.getRegionWidth();
		imageHeight = image.getRegionHeight();

		live = MAX_LIVE;
	}

	public void set(int value) {
		live = value;
	}
	
	public void reset(){
		set(MAX_LIVE);
	}

	public void decrease(int amount) {
		live -= amount;
		live = live > 0 ? live : 0;
	}

	public void increase(float amount) {
		live += amount;
		live = live < MAX_LIVE ? live : MAX_LIVE;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// change batch alpha based of parent alpha
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float xHeart = x;
		for (int i = 0; i < live; i++) {
			batch.draw(R.assets.image("ui/heart"), xHeart + 2, y + 2, imageWidth - 2, imageHeight - 2);
			xHeart += (imageWidth + offset);
		}
		float xBorder = x;
		for (int i = 0; i < MAX_LIVE; i++) {
			batch.draw(R.assets.image("ui/empty_heart"), xBorder, y, imageWidth, imageHeight);
			xBorder += (imageWidth + offset);
		}
	}

	public float getAmount() {
		return live;
	}

}
