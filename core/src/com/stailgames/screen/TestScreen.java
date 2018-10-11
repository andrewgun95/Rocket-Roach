package com.stailgames.screen;

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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.stailgames.character.Cockroach;
import com.stailgames.debug.CameraHelper;
import com.stailgames.debug.Info;
import com.stailgames.gameobject.Movement;
import com.stailgames.gameobject.Movement.Speed;
import com.stailgames.main.World;
import com.stailgames.gameobject.UI;

public class TestScreen extends AbstractScreen {

	private Stage stage;
	private UI ui;
	private Cockroach cockroach;

	public TestScreen(){
		super(null);
	}
	
	public TestScreen(Game game){
		super(game);
	}
	
	@Override
	public void show() {

		stage = new Stage(new FillViewport(World.WIDTH, World.HEIGHT));
		Movement.init(new Speed(-0.04f, 10f, -0.08f, 0.005f));

		cockroach = new Cockroach(-0.5f, 0.5f);
		cockroach.setLimit(0.3f, World.HEIGHT);
		cockroach.addAction(Actions.moveBy(1.0f, 0.0f, 1.0f));

		ui = new UI();

		stage.addActor(cockroach);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(ui.getStage());
		multiplexer.addProcessor(new Controller());
		Gdx.input.setInputProcessor(multiplexer);
		
		stage.setDebugAll(true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);

		stage.act(delta);
		stage.draw();

		
		ui.render();

		CameraHelper.update((OrthographicCamera) stage.getCamera());
		Info.drawCamera(stage);
		Info.render();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, false);

	}

	@Override
	public void pause() {
	}

	@Override
	public void hide() {
	}

	private class Controller extends InputAdapter {

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			Stage uistage = ui.getStage();
			Actor uiactor = uistage.hit((float) screenX, World.getScreenHeight() - (float) screenY, true);
			if (uiactor != null) {
				return false;
			}
			cockroach.eventBoost(true);
			return true;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			cockroach.eventBoost(false);
			return true;
		}

		@Override
		public boolean keyDown(int keycode) {
			if(keycode == Keys.ENTER){
				System.out.println("Test");
				cockroach.activated(false);
			}
			return true;
		}

		
		
	}
}
