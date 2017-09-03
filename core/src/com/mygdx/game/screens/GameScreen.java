package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Mathsplosive;

public class GameScreen implements Screen {
	
	public static float SPEED = 40;

	Texture img;
	float x = 0, y = 0;
	
	Mathsplosive game;
	
	public GameScreen(Mathsplosive game) {
		this.game = game;
	}

	@Override
	public void show() {
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render(float delta) {
		// Command to get the delta time, which is the time between rendered frames
		// isKeyPressed means is it pressed at that instant
		// if isKeyJustPressed means that is it just recently pressed last frame
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			y += SPEED * Gdx.graphics.getDeltaTime();
		}

		// Runs render at every frame
		// OpenGL stuff
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Start drawing images to the screen
		game.batch.begin();

		// Drawing the variable img, at x, y, which is at the bottom left corner
		game.batch.draw(img, x, y);

		// End of drawing images to the screen
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
