package com.stailgames.gameobject;

import static com.stailgames.utils.Converter.pixelToMeter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.stailgames.main.R;
import com.badlogic.gdx.utils.Pools;

public class ParallaxBG extends Actor {

	public final float width, height;

	private Layer building;
	private Layer shadow;
	private Layer stars;

	public ParallaxBG(float width, float height) {
		this.width = width;
		this.height = height;

		building = new Layer(0.2f, Movement.getSpeed().game, false, 
				new TextureRegion[] { 
						R.assets.image("background/building-1"),
						R.assets.image("background/building-2"),
						R.assets.image("background/building-3"), 
						R.assets.image("background/building-4") }
		);

		shadow = new Layer(0.3f, Movement.getSpeed().game * 0.8f, false, 
				new TextureRegion[] { 
						R.assets.image("background/shadow-1"), 
						R.assets.image("background/shadow-2"), 
						R.assets.image("background/shadow-3"), 
						R.assets.image("background/shadow-4") }
		);

		stars = new Layer(2.3f, Movement.getSpeed().game * 0.08f, false, 
				new TextureRegion[] { 
						R.assets.image("background/star-1"),
						R.assets.image("background/star-2"), 
						R.assets.image("background/star-3"), 
						R.assets.image("background/star-4") }
		);

	}

	
	
	@Override
	public void act(float delta) {
		stars.act(delta);
		shadow.act(delta);
		building.act(delta);
	}


	@Override
	public void draw(Batch batch, float parentAlpha) {
		building.set(0.2f, Movement.getSpeed().game);
		shadow.set(0.3f, Movement.getSpeed().game * 0.8f);

		batch.draw(R.assets.image("background/background"), 0, 0, width, height);
		stars.draw(batch, parentAlpha);
		shadow.draw(batch, parentAlpha);
		building.draw(batch, parentAlpha);
	}

	private class Layer extends Actor {

		private final TextureRegion[] images;

		private Array<Model> models = new Array<Model>();
		private int counter;

		private float elevation;
		private float speed;
		private boolean random;

		public Layer(float elevation, float speed, boolean random, TextureRegion... images) {
			this.images = images;
			this.random = random;
			set(elevation, speed);

			counter = 0;

			float x = 0.0f;

			do {
				TextureRegion image = images[random ? MathUtils.random(images.length - 1) : counter % images.length];

				Model model = Model.instance(x, elevation, image);
				models.add(model);

				x += model.getWidth();
				counter++;
			} while (x < width);
		}

		public void set(float elevation, float speed) {
			this.elevation = elevation;
			this.speed = speed;
		}

		private void create() {
			if (models.size == 0) return;

			Model model = models.peek();
			if (model.getX() + model.getWidth() < width) {
				TextureRegion image = images[random ? MathUtils.random(images.length - 1) : counter % images.length];
				Model instance = Model.instance(model.getX() + model.getWidth(), elevation, image);
				models.add(instance);
				counter++;
			}
		}

		@Override
		public void act(float delta){
			create();
			for (Model model : models)
				model.moveBy(speed, 0.0f);
			destroy();
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			for (Model model : models)
				model.draw(batch, parentAlpha);
		}

		private void destroy() {
			if (models.size == 0) return;

			Model model = models.first();
			if (model.getX() + model.getWidth() < 0.0f) {
				models.removeValue(model, false);
				Pools.free(model);
			}
		}

	}

	private static class Model extends Actor implements Poolable {

		public static Model instance(float x, float y, TextureRegion image) {
			Model model = Pools.get(Model.class).obtain();
			model.init(x, y, image);
			return model;
		}

		private TextureRegion image;
		private float imageWidth;
		private float imageHeight;

		private void init(float x, float y, TextureRegion image) {
			this.image = image;
			this.imageWidth = pixelToMeter(image.getRegionWidth());
			this.imageHeight = pixelToMeter(image.getRegionHeight());

			setPosition(x, y);
			setBounds(getX(), getY(), imageWidth, imageHeight);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(image, getX(), getY(), imageWidth, imageHeight);
			
			setBounds(getX(), getY(), imageWidth, imageHeight);
		}

		@Override
		public boolean equals(Object model) {
			return getX() == ((Model) model).getX() && getY() == ((Model) model).getY();
		}

		@Override
		public void reset() {
			setPosition(0, 0);
		}

	}

}
