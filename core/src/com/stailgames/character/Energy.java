package com.stailgames.character;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.stailgames.main.R;

public class Energy {

	private static final float MAX_ENERGY = 1000;

	private float energy;

	public Energy() {
		setMAX();
	}

	public void increase(float amount) {
		energy = energy < MAX_ENERGY ? energy + amount : MAX_ENERGY;
	}

	public void setMAX() {
		energy = MAX_ENERGY;
	}

	public void setHalfMAX() {
		if(energy + (MAX_ENERGY / 2) > MAX_ENERGY){
			energy = MAX_ENERGY;
			return;
		}
		energy += MAX_ENERGY / 2;
	}

	public void decrease(float amount) {
		energy = energy > 0 ? energy - amount : 0f;
	}

	public boolean isEmpty() {
		return energy == 0;
	}

	public void draw(Batch batch, float x, float y) {
		batch.draw(R.assets.image("character/energy"), x, y, pixelToMeter(51) * (energy / 1000f), pixelToMeter(11));
		batch.draw(R.assets.image("character/energy_bar"), x, y, pixelToMeter(51), pixelToMeter(11));
	}

}
