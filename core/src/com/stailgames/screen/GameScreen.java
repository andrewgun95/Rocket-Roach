package com.stailgames.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.stailgames.character.Cockroach;
import com.stailgames.debug.CameraHelper;
import com.stailgames.debug.Info;
import com.stailgames.gameobject.Detector;
import com.stailgames.gameobject.Environment;
import com.stailgames.gameobject.Movement;
import com.stailgames.gameobject.Movement.Speed;
import com.stailgames.gameobject.ParallaxBG;
import com.stailgames.gameobject.Platform;
import com.stailgames.gameobject.ThrowingObject;
import com.stailgames.gameobject.Title;
import com.stailgames.gameobject.UI;
import com.stailgames.main.R;
import com.stailgames.main.World;

public class GameScreen extends AbstractScreen {

	private static boolean DEBUG = false;

	public enum STATE {
		TITLE, START, OVER;
	}

	private Stage stage;

	// game user interface and screen
	private Title title;
	private UI ui;

	// game component
	private ParallaxBG background;
	private Cockroach cockroach;
	private Platform platform;
	private Environment environment;
	private ThrowingObject throwing;

	private Detector detector;

	private STATE state;

	private boolean pause;
	private boolean option = true;

	public GameScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {

		Movement.init(new Speed(-0.04f, 10f, -0.08f, 0.005f));

		stage = new Stage(new FillViewport(World.WIDTH, World.HEIGHT));
		stage.setDebugAll(DEBUG);

		// game user-interface and screen
		title = new Title();
		ui = new UI();

		// game component
		background = new ParallaxBG(World.WIDTH, World.HEIGHT);
		cockroach = new Cockroach(-0.5f, 0.4f);

		platform = new Platform(-0.1f);
		environment = new Environment();
		throwing = new ThrowingObject(5.0f, cockroach);

		detector = new Detector(cockroach, environment, platform, ui, throwing);
		stage.addActor(platform);
		stage.addActor(background);
		stage.addActor(environment);
		stage.addActor(throwing);
		stage.addActor(cockroach);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(title.getStage());
		multiplexer.addProcessor(ui.getStage());
		multiplexer.addProcessor(new Controller());

		Gdx.input.setInputProcessor(multiplexer);

		state = STATE.TITLE;
		pause = false;
		init();
	}

	private void init() {
		environment.activated(false);
		throwing.activated(false);
		cockroach.setLimit(platform.getLowerLimit() + cockroach.getBoundHeight() / 2, platform.getUpperLimit() - cockroach.getBoundHeight() / 2);

		ui.getBoard().getRetry().addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {

				Movement.init(new Speed(-0.04f, 10f, -0.08f, 0.005f));

				ui.hideBoard();
				detector.reset();
				ui.reset();

				cockroach.clearActions();
				cockroach.setPosition(-0.5f, 0.4f);
				cockroach.addAction(Actions.moveBy(1.0f, 0.0f, 1.0f));

				stage.addAction(delay(1.0f, run(new Runnable() {

					public void run() {

						environment.reset();
						throwing.reset();
						cockroach.activated(true);
						cockroach.getEnergy().setMAX();
						cockroach.eventSmoke(false);
						
						temp = true;

					}

				})));

				state = STATE.START;

			}

		});
		
		ui.getPause().addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				pause = !pause;
				if (ui.getPause().isTouchable()) {
					if (option) {
						ui.showOption();
						option = false;
					} else {
						ui.hideOption();
						option = true;
					}
				}
			}

		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		debugController();
		if (!pause) stage.act(delta);
		stage.draw();

		switch (state) {
		case TITLE:
			title.render();
			if (title.isOver()) {

				environment.activated(true);
				throwing.activated(true);

				cockroach.addAction(Actions.moveBy(1.0f, 0.0f, 1.0f));

				R.assets.music().play();
				R.assets.music().setLooping(true);

				state = STATE.START;
			}
			break;
		case START:
			ui.render();
			if (!pause) detector.response(delta);

			if (!detector.isActive()) {
				environment.activated(false);
				throwing.activated(false);
				cockroach.activated(false);
				cockroach.eventSmoke(true);

				if (temp) {
					R.assets.soundMp3("damage").play();
					temp = false;
				}

				if (environment.isClear()) {
					ui.showBoard();
					cockroach.addAction(Actions.moveBy(-1.0f, 0.0f, 2.0f));
					state = STATE.OVER;
				}
			}
			break;
		case OVER:
			ui.render();
			break;
		default:
			break;
		}

		debugTimer(delta);
		showCamera();
		showZOrder();
		Info.render();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, false);
	}

	private int timer = 0;
	private float tick = 0.0f;

	private void debugTimer(float delta) {
		if ((tick += delta) > 1f) {
			timer++;
			tick = 0.0f;
		}
		Info.drawString("TIME : " + timer, 600, 20);
	}

	private void showCamera() {
		CameraHelper.update((OrthographicCamera) stage.getCamera());
	}

	private void showZOrder() {
		String names = "";
		for (Actor actor : stage.getActors()) {
			if (actor != null) names += "[" + actor.getName() + "," + actor.getZIndex() + "]";
		}

		Info.drawString(names, 300, 5);
	}

	private void debugController() {
		if (Gdx.input.isKeyJustPressed(Keys.P)) {
			Movement.speed(1.2f);
		}
		if (Gdx.input.isKeyJustPressed(Keys.L)) {
			Movement.speed(0.8f);
		}
		if (Gdx.input.isKeyJustPressed(Keys.O)) {
			stage.setDebugAll(DEBUG = !DEBUG);
			Info.setVisible(!Info.getVisible());
		}
		Info.drawString("PRESS P : INCREASE SPEED", 500, 50);
		Info.drawString("PRESS O : DEBUG MODE (ON/OFF)", 500, 60);
	}

	@Override
	public void pause() {
	}

	@Override
	public void hide() {
		ui.getUIDistance().save();
		ui.getScore().save();
		dispose();
		pause = false;
	}

	private class Controller extends InputAdapter {

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			Stage uistage = ui.getStage();
			Actor uiactor = uistage.hit((float) screenX, World.getScreenHeight() - (float) screenY, true);
			if (uiactor != null) {
				return false;
			}
			Stage titlestage = title.getStage();
			Actor titleactor = titlestage.hit((float) screenX, World.getScreenHeight() - (float) screenY, true);
			if (titleactor != null) {
				return false;
			}
			if (pause) {
				ui.hideOption();
				pause = false;
				option = true;
			}
			cockroach.eventBoost(true);
			return true;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			cockroach.eventBoost(false);
			return true;
		}

	}

	public boolean temp = true;
}
