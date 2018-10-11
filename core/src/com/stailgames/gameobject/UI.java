package com.stailgames.gameobject;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.stailgames.components.Distance;
import com.stailgames.components.Live;
import com.stailgames.components.Score;
import com.stailgames.main.R;
import com.stailgames.main.World;

public class UI {

	private Stage stage;

	// head-up display
	private Score score;
	private Live live;
	private Image coin;
	private Distance distance;

	// user-interface
	private Button pause;
	private Board board;

	private Option option;

	public UI() {
		stage = new Stage(new FillViewport(World.getScreenWidth(), World.getScreenHeight()));

		score = new Score(50, 427);
		live = new Live(10, 440, 5);

		coin = new Image(R.assets.uiskin("coin"));
		coin.setPosition(10, 400);

		distance = new Distance(10, 385);

		board = new Board();
		board.setPosition(240, 120);

		pause = new Button(R.assets.uiskin("pause-1"), R.assets.uiskin("pause-2"));
		pause.setPosition(740, 420);

		option = new Option();
		option.setPosition(285, 180);

		stage.addActor(score);
		stage.addActor(live);
		stage.addActor(distance);
		stage.addActor(coin);
		stage.addActor(pause);

	}

	public void render() {
		stage.act();
		stage.draw();
	}

	public void showOption() {
		stage.addActor(option);
	}

	public void hideOption() {
		option.remove();
	}

	public void showBoard() {
		board.getColor().a = 0.0f;
		board.addAction(parallel(alpha(1.0f, 0.2f), moveBy(0.0f, -50f)));
		stage.addActor(board);
	}

	public void hideBoard() {
		distance.save();
		board.remove();
		board.setPosition(240, 120);
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	public void reset() {
		live.reset();
		distance.reset();
	}

	public Score getScore() {
		return score;
	}

	public Live getLive() {
		return live;
	}

	public Button getExit() {
		return pause;
	}

	public Button getPause() {
		return pause;
	}

	public Board getBoard() {
		return board;
	}

	public Distance getUIDistance() {
		return distance;
	}

	public Stage getStage() {
		return stage;
	}

	public class Board extends Group {

		private Image background;
		private Button rate;
		private Button retry;

		private BitmapFont font;

		public Board() {
			background = new Image(R.assets.uiskin("scoreboard"));

			rate = new Button(R.assets.uiskin("rate-1"), R.assets.uiskin("rate-2"));
			rate.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.stailgames.rocketroach");
				}

			});
			retry = new Button(R.assets.uiskin("retry-1"), R.assets.uiskin("retry-2"));

			addActor(background);
			addActor(rate);
			addActor(retry);

			// position related this actor
			rate.setPosition(75f, 25f);
			retry.setPosition(200f, 25f);

			font = R.assets.font32();
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			super.draw(batch, parentAlpha);
			font.draw(batch, "Your Distance :  " + distance.getDistance(), getX() + 20f, getY() + 250f);
			font.draw(batch, "Best Distance : " + distance.getBestDistance(), getX() + 20f, getY() + 200f);
			font.draw(batch, "Coins : " + score.getValue(), getX() + 20f, getY() + 150f);
		}

		public Button getRetry() {
			return retry;
		}

		public Button getRate() {
			return rate;
		}

	}

	public class Option extends Group {

		private Image background;
		private Button silent;
		private Button exit;

		public Option() {
			background = new Image(R.assets.uiskin("option"));

			exit = new Button(R.assets.uiskin("quit-1"), R.assets.uiskin("quit-2"));
			exit.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					Gdx.app.exit();
				}
			});
			silent = new Button(R.assets.uiskin("mute-1"), R.assets.uiskin("mute-1"), R.assets.uiskin("mute-2"));
			silent.addListener(new ChangeListener() {

				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if (silent.isChecked()) {
						R.assets.music().setVolume(0.0f);
					} else
						R.assets.music().setVolume(1.0f);

				}
			});

			addActor(background);
			addActor(exit);
			addActor(silent);

			// position related this actor
			exit.setPosition(50f, 50f);
			silent.setPosition(125f, 50f);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			super.draw(batch, parentAlpha);
		}

		public Button getExit() {
			return exit;
		}

		public Button getSilent() {
			return silent;
		}

	}

}
