package com.stailgames.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.stailgames.main.R;

public class Distance extends Component {

	private BitmapFont font;

	private Preferences data;

	private int number;
	private int oldnumber;

	private float x, y;

	public Distance(float x, float y) {
		this.x = x;
		this.y = y;

		number = 0;

		data = Gdx.app.getPreferences("setting.prefs");

		font = R.assets.font32();
	}

	public void set(int value) {
		number = value;
	}

	public void increase() {
		number++;
	}

	public int getDistance() {
		return number;
	}

	public int getBestDistance() {
		// update old number
		oldnumber = data.getInteger("bestdistance", 0);
		return number > oldnumber ? number : oldnumber;
	}

	public void save() {		
		data.putInteger("bestdistance", getBestDistance());
		data.flush();
	}

	public void reset() {
		set(0);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.draw(batch, "Distance : " + number + " m", x, y);
	}

}
