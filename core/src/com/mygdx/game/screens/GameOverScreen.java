package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Mathsplosive;
import com.mygdx.game.tools.ScrollingBackground;

public class GameOverScreen implements Screen{
	
	private static final int BANNER_WIDTH = 350;
	private static final int BANNER_HEIGHT = 100;
	
	Texture gameOverBanner;
	BitmapFont scoreFont;
	Mathsplosive game;
	int score, highscore;
	Preferences prefs = Gdx.app.getPreferences("mathsplosive");
	
	public GameOverScreen (Mathsplosive game, int score) {
		this.game = game;
		this.score = score;
		
		// Will look for highscore tag and get integer. If no tag, set highscore to 0
		highscore = prefs.getInteger("highscore", 0);
		
		if(score > highscore) {
			prefs.putInteger("highscore", score);
			// Saves file ONLY after flush
			prefs.flush();
		}
		
		gameOverBanner = new Texture("game_over.png");
		scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
		
		game.scrollingBackground.setSpeedFixed(true);
		game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
	}
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		game.batch.begin();
		
		game.scrollingBackground.updateAndRender(delta, game.batch);
		
		game.batch.draw(gameOverBanner, Gdx.graphics.getWidth() / 2 - BANNER_WIDTH / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT - 15, BANNER_WIDTH, BANNER_HEIGHT);
		
		// TODO: Find the constructors meaning
		// GlyphLayout draws from top left, instead of bottom left
		GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: \n" + score, Color.WHITE, 0, Align.left, false);
		GlyphLayout highScoreLayout = new GlyphLayout(scoreFont, "Highscore: \n" + prefs.getInteger("highscore", 0), Color.WHITE, 0, Align.left, false);
	
		// Find out what this means
		scoreFont.draw(game.batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT - 15 * 2);
		scoreFont.draw(game.batch, highScoreLayout, Gdx.graphics.getWidth() / 2 - highScoreLayout.width / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT - scoreLayout.height - 15 * 3);
		
		GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
		GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");
		
		float tryAgainX = Gdx.graphics.getWidth() / 2 - tryAgainLayout.width /2;
		float tryAgainY = Gdx.graphics.getHeight() / 2 - tryAgainLayout.height / 2;
		float mainMenuX = Gdx.graphics.getWidth() / 2 - mainMenuLayout.width /2;
		// Padding for the Y, or else it would be on the same spot
		float mainMenuY = Gdx.graphics.getHeight() / 2 - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;
		
		// Where the player's cursor is placed. Height of screen - Y is because 0 is on top, so to get height from the bottom have to minus the height
		float touchX = Gdx.input.getX(), touchY = Gdx.graphics.getHeight() - Gdx.input.getY();
		
		if(Gdx.input.isTouched()) {
			if(touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
				// Throw screen away, and open a new GameScreen
				this.dispose();
				game.batch.end();
				game.setScreen(new GameScreen(game));
				return;
			}
			
			if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
				this.dispose();
				game.batch.end();
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
		
		scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
		scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);
		
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
