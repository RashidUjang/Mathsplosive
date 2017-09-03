package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Mathsplosive;

public class GameScreen implements Screen {
	
	// Speed for movement of image. 40 pixels per second
	public static float SPEED = 40;
	public static int SHIP_WIDTH_PIXEL = 17;
	public static int SHIP_HEIGHT_PIXEL = 32;
	// 17 * 3, because the spritesheet for ship is 17
	public static int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
	// 32 * 3, because the spritesheet for ship is 32
	public static int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;
	
	// Animation speed. 0.5 secs per frame
	public static float SHIP_ANIMATION_SPEED = 0.5f;
	
	Animation<TextureRegion>[] rolls;
	
	float x = 0, y = 0;
	float stateTime;
	
	// an integer roll to keep track of the roll of the sprite
	int roll;
	
	Mathsplosive game;
	
	public GameScreen(Mathsplosive game) {
		this.game = game;
		y = 15;
		x = Mathsplosive.WIDTH / 2 - SHIP_WIDTH / 2;
		
		roll = 2;
		rolls = new Animation[5];
		
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);
	
		rolls[roll] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]);
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		// Command to get the delta time, which is the time between rendered frames
		// isKeyPressed means is it pressed at that instant
		// if isKeyJustPressed means that is it just recently pressed last frame
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			y += SPEED * Gdx.graphics.getDeltaTime();
		}
		
		stateTime += delta;
		// Runs render at every frame
		// OpenGL stuff
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Start drawing images to the screen
		game.batch.begin();
		
		game.batch.draw(rolls[roll].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);
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
