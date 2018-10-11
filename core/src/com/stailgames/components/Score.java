package com.stailgames.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.stailgames.main.R;

/**
 * this component in pixel, not in meter
 *
 */
public class Score extends Component {

	public static final int MAX_SCORE = 1000;

	public float x, y;
	
	private Preferences data;

	public BitmapFont font;
	public int score;
	public int oldscore;

	public Score(float x, float y) {
		this.x = x;
		this.y = y;

		font = R.assets.font32();
		
		data = Gdx.app.getPreferences("settings.prefs");
		oldscore = data.getInteger("oldscore", 0);
		
		score = oldscore;
	}

	public void increase() {
		score++;
		score = score < MAX_SCORE ? score : MAX_SCORE;
	}
	
	public int getValue(){
		return score;
	}
	
	public void set(int value){
		score = value;
	}
	
	public void reset(){
		set(0);
	}

	public void save() {		
		data.putInteger("oldscore", score);
		data.flush();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		// set font alpha from parent alpha
		Color color = getColor();
		font.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		String text = score + "";
		font.draw(batch, text, x, y);
	}

}
