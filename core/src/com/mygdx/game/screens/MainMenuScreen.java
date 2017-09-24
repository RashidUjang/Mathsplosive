package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Mathsplosive;
import com.mygdx.game.tools.ScrollingBackground;

public class MainMenuScreen implements Screen{
	private static final int EXIT_BUTTON_WIDTH = 300;
	private static final int EXIT_BUTTON_HEIGHT = 150;
	private static final int PLAY_BUTTON_WIDTH = 330;
	private static final int PLAY_BUTTON_HEIGHT = 150;
	
	// Declare the height of the button from the bottom of the screen
	private static final int EXIT_BUTTON_Y = 100;
	private static final int PLAY_BUTTON_Y = 300;

	Mathsplosive game;
	
	Texture exitButtonActive;
	Texture exitButtonInactive;
	Texture playButtonActive;
	Texture playButtonInactive; 
	
	public MainMenuScreen(Mathsplosive game) {
		this.game = game;
		
		// Assigning textures from asset to declared textures
		exitButtonActive = new Texture("exit_button_active.png");
		exitButtonInactive = new Texture("exit_button_inactive.png");
		playButtonActive = new Texture("play_button_active.png");
		playButtonInactive = new Texture("play_button_inactive.png");
	
		game.scrollingBackground.setSpeedFixed(true);
		game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		// Set the colour for the background. The order is R G B
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		game.batch.begin();
		
		game.scrollingBackground.updateAndRender(delta, game.batch);
		// Getting the 
		int x = Mathsplosive.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2; 
		
		if(Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && Mathsplosive.HEIGHT - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && Mathsplosive.HEIGHT - Gdx.input.getY() > EXIT_BUTTON_Y) {
			// Drawing Textures, with parameters being x, y, width, height
			game.batch.draw(exitButtonActive, ((Mathsplosive.WIDTH / 2) - (EXIT_BUTTON_WIDTH / 2)), EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
			
			if (Gdx.input.isTouched()) {
				Gdx.app.exit();
			}
		} else {
			game.batch.draw(exitButtonInactive, ((Mathsplosive.WIDTH / 2) - (EXIT_BUTTON_WIDTH / 2)), EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
		
			if (Gdx.input.justTouched()) {
				// Call dispose to discard screen after its done
				this.dispose();
				game.setScreen(new GameScreen(game));
			}
		}
		
		x = Mathsplosive.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2; 
		if(Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x && Mathsplosive.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && Mathsplosive.HEIGHT - Gdx.input.getY() > PLAY_BUTTON_Y) {
			game.batch.draw(playButtonActive, ((Mathsplosive.WIDTH / 2) - (PLAY_BUTTON_WIDTH / 2)), PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
		} else {
		game.batch.draw(playButtonInactive, ((Mathsplosive.WIDTH / 2) - (PLAY_BUTTON_WIDTH / 2)), PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
		}
		
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}
}
