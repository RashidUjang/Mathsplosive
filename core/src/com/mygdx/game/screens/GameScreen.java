package com.mygdx.game.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Mathsplosive;
import com.mygdx.game.entities.Bullet;

public class GameScreen implements Screen {
	
	// Speed for movement of image. 40 pixels per second
	public static final float SPEED = 300;
	public static final int SHIP_WIDTH_PIXEL = 17;
	public static final int SHIP_HEIGHT_PIXEL = 32;
	// 17 * 3, because the spritesheet for ship is 17
	public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
	// 32 * 3, because the spritesheet for ship is 32
	public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;
	public static final float SHOOT_WAIT_TIME = 0.3f;
	
	// Animation speed. 0.5 secs per frame
	public static final float SHIP_ANIMATION_SPEED = 0.5f;
	
	// 0.15 seconds per switch to next animation
	public static final float ROLL_TIMER_SWITCH_TIMER = 0.10f;
	Animation<TextureRegion>[] rolls;
	
	float x = 0, y = 0;
	float stateTime;
	float rollTimer;
	float shootTimer;
	
	// an integer roll to keep track of the roll of the sprite
	int roll;
	
	ArrayList<Bullet> bullets;
	
	Mathsplosive game;
	
	public GameScreen(Mathsplosive game) {
		this.game = game;
		y = 15;
		x = Mathsplosive.WIDTH / 2 - SHIP_WIDTH / 2;
		bullets = new ArrayList<Bullet>();
		shootTimer = 0;
		
		roll = 2;
		rollTimer = 0;
		rolls = new Animation[5];
		
		// Splitting the texture by its boxes.
		TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);
	
		rolls[0] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]); // All left
		rolls[1] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]);
		rolls[2] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]); // No tilt
		rolls[3] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]);
		rolls[4] = new Animation<TextureRegion>(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]); // All right
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		shootTimer += delta;
		
		if (Gdx.input.isKeyPressed(Keys.SPACE) && shootTimer >= SHOOT_WAIT_TIME) {
			shootTimer = 0;
			bullets.add(new Bullet(x + 4));
			bullets.add(new Bullet(x + SHIP_WIDTH - 4));
		}
		
		ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
		
		for (Bullet bullet : bullets) {
			bullet.update(delta);
			
			if(bullet.remove) {
				bulletsToRemove.add(bullet);
			}
		}
		
		bullets.remove(bulletsToRemove);
		// Command to get the delta time, which is the time between rendered frames
		// isKeyPressed means is it pressed at that instant
		// if isKeyJustPressed means that is it just recently pressed last frame
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			y += SPEED * Gdx.graphics.getDeltaTime();
		}
		
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			y -= SPEED * Gdx.graphics.getDeltaTime();
		}
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			x -= SPEED * Gdx.graphics.getDeltaTime();
			
			if (x < 0) {
				x = 0;
			}
			
			// Check if key is JUST pressed (happens during first frame its pressed) and if no right key is being pressed
			if (Gdx.input.isKeyJustPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT) && roll > 0) {
				rollTimer = 0;
				roll--;	
			}
			// Minus because going left. 
			rollTimer -= Gdx.graphics.getDeltaTime();
			
			// If 0.15 secs have passed, reset timer and check if its 0 to not roll it too far
			if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIMER && roll > 0) {
				rollTimer -= ROLL_TIMER_SWITCH_TIMER;
				roll--;
			}
		} else {
			if (roll < 2) {
				rollTimer += Gdx.graphics.getDeltaTime();
				
				if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIMER && roll < 4) {
					rollTimer -= ROLL_TIMER_SWITCH_TIMER;
					roll++;
								
					if (roll > 4) {
						roll = 4;
					}
				}
			}
		}
		
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			x += SPEED * Gdx.graphics.getDeltaTime();
			
			if (x + SHIP_WIDTH > Gdx.graphics.getWidth()) {
				x = Gdx.graphics.getWidth() - SHIP_WIDTH;
			}
			
			// Check if key is JUST pressed (happens during first frame its pressed) and if no right key is being pressed
			if (Gdx.input.isKeyJustPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT) && roll < 4) {
				rollTimer = 0;
				roll++;	
			}
						
			// Plus because going right. 
			rollTimer += Gdx.graphics.getDeltaTime();
						
			// If 0.15 secs have passed, reset timer and check if its 0 to not roll it too far
			if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIMER && roll < 4) {
				rollTimer -= ROLL_TIMER_SWITCH_TIMER;
				roll++;
			}
		} else {
			if (roll > 2) {
				rollTimer -= Gdx.graphics.getDeltaTime();
				
				if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIMER && roll > 0) {
					rollTimer -= ROLL_TIMER_SWITCH_TIMER;
					roll--;
				}
			}
		}
		
		stateTime += delta;
		// Runs render at every frame
		// OpenGL stuff
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Start drawing images to the screen
		game.batch.begin();
		
		for (Bullet bullet : bullets) {
			bullet.render(game.batch);
		}
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