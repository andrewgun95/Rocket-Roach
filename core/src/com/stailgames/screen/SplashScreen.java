package com.stailgames.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.stailgames.main.World;

public class SplashScreen extends AbstractScreen {

	private Stage stage;

	private Skin skin;
	private TextureAtlas images;

	private Table layout;

	private Image star;
	private Image snail;

	private Image sta;
	private Image ilgames;

	private Sound sfx1;
	private Sound sfx2;

	public SplashScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		stage = new Stage(new FillViewport(World.getScreenWidth(), World.getScreenHeight()));
		images = new TextureAtlas(Gdx.files.internal("logo/logo.atlas"));
		skin = new Skin(images);

		sfx1 = Gdx.audio.newSound(Gdx.files.internal("logo/sfx1.ogg"));
		sfx2 = Gdx.audio.newSound(Gdx.files.internal("logo/sfx2.ogg"));

		// star
		star = new Image(skin.getDrawable("star"));

		// symbol
		snail = new Image(skin.getDrawable("snail"));

		// sentences
		sta = new Image(skin.getDrawable("sta"));
		ilgames = new Image(skin.getDrawable("ilgames"));

		layout = new Table();
		layout.add(snail).colspan(2);
		layout.row();
		layout.add(sta);
		layout.add(ilgames).spaceTop(15);

		// initial star
		star.setPosition(50, 50);
		star.setOrigin(Align.center);
		star.getColor().a = 0.0f;

		// initial symbol
		snail.getColor().a = 0.0f;

		// initial sentences
		sta.getColor().a = 0.0f;
		ilgames.getColor().a = 0.0f;

		// star movement actions
		SequenceAction movement = new SequenceAction();
		movement.addAction(moveBy(10f, 50f, 0.1f));
		movement.addAction(moveBy(15f, 60f, 0.1f));
		movement.addAction(moveBy(20f, 50f, 0.1f));
		movement.addAction(moveBy(25f, 40f, 0.1f));
		movement.addAction(moveBy(30f, 30f, 0.1f));
		movement.addAction(moveBy(35f, 20f, 0.1f));
		movement.addAction(moveBy(40f, 10f, 0.1f));
		movement.addAction(moveBy(45f, .0f, 0.1f));
		movement.addAction(moveBy(50f, 10f, 0.1f));
		movement.addAction(moveBy(55f, 15f, 0.1f));
		movement.addAction(moveBy(60f, 18f, 0.1f));
		movement.addAction(moveBy(20f, 0f, 0.5f));

		star.addAction(sequence(parallel(movement, fadeIn(3.0f), rotateBy(-100f, 1.0f), delay(1.5f, rotateBy(90f, 1.0f)), run(new Runnable() {

			public void run() {
				sfx1.play();
			}

		})), run(new Runnable() {

			public void run() {
				sfx2.play();
			}
			
		})));

		snail.addAction(delay(3.0f, fadeIn(1.0f)));
		sta.addAction(delay(3.5f, fadeIn(1.0f)));
		ilgames.addAction(sequence(delay(4.0f, fadeIn(1.0f)), new RunnableAction() {

			@Override
			public void run() {
				game.setScreen(new LoadingScreen(game));
			}

		}));

		layout.setFillParent(true);
		stage.addActor(layout);
		stage.addActor(star);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);

		// if (!star.hasActions()) {
		//
		// SequenceAction action = new SequenceAction();
		//
		// SequenceAction movement = new SequenceAction();
		// movement.addAction(moveBy(10f, 50f, 0.5f));
		// movement.addAction(moveBy(15f, 60f, 0.5f));
		// movement.addAction(moveBy(20f, 50f, 0.5f));
		// movement.addAction(moveBy(25f, 40f, 0.5f));
		// movement.addAction(moveBy(30f, 30f, 0.5f));
		// movement.addAction(moveBy(35f, 20f, 0.5f));
		// movement.addAction(moveBy(40f, 10f, 0.5f));
		// movement.addAction(moveBy(45f, .0f, 0.5f));
		// movement.addAction(moveBy(50f, 10f, 0.5f));
		// movement.addAction(moveBy(55f, 15f, 0.5f));
		// movement.addAction(moveBy(60f, 18f, 0.5f));
		// movement.addAction(moveBy(20f, 0f, 1.0f));
		//
		// action.addAction(movement);
		// action.addAction(new RunnableAction() {
		//
		// @Override
		// public void run() {
		// star.clearActions();
		// star.setPosition(50, 50);
		// }
		//
		// });
		//
		// star.addAction(action);
		//
		// }

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void hide() {
		images.dispose();
		skin.dispose();
		sfx1.dispose();
		sfx2.dispose();
	}

}
