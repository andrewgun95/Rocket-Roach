package com.stailgames.gameobject;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.addAction;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.stailgames.main.R;
import com.stailgames.main.World;

public class Title {

	private Stage stage;

	private Image screen;
	private Button tap;

	private boolean state;

	public Title() {
		stage = new Stage(new FillViewport(World.getScreenWidth(), World.getScreenHeight()));

		screen = new Image(R.assets.uiskin("titlescreen"));
		screen.setPosition(World.getScreenWidth() / 2, World.getScreenHeight() / 2, Align.center);
		tap = new Button(R.assets.uiskin("button-tap"));
		tap.setPosition(World.getScreenWidth() / 2, 100, Align.center);

		stage.addActor(screen);
		stage.addActor(tap);

		stage.addListener(new InputListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// button effect
				SequenceAction beffect = new SequenceAction();
				beffect.addAction(moveBy(0.0f, -5.0f, 0.1f));
				beffect.addAction(moveBy(0.0f, 5.0f, 0.1f));

				// fade effect
				ParallelAction feffect = new ParallelAction();
				feffect.addAction(addAction(alpha(0.0f, 1.0f), screen));
				feffect.addAction(alpha(0.0f));

				RunnableAction trigger = new RunnableAction() {

					@Override
					public void run() {
						screen.remove();
						tap.remove();
						state = true;
					}

				};

				tap.addAction(sequence(beffect, feffect, delay(1.0f, trigger)));
				stage.removeListener(this);
				return true;
			}

		});

		state = false;
	}

	public void render() {
		stage.act();
		stage.draw();
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	public Stage getStage() {
		return stage;
	}

	public boolean isOver() {
		return state;
	}

}
