package com.mygdx.game.screens;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Mathsplosive;
import com.mygdx.game.entities.Asteroid;
import com.mygdx.game.entities.Bullet;
import com.mygdx.game.entities.Explosion;
import com.mygdx.game.tools.CollisionRect;

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
	public static final float MIN_ASTEROID_SPAWN_TIME = 0.3f;
	public static final float MAX_ASTEROID_SPAWN_TIME = 0.6f;
	
	// 0.15 seconds per switch to next animation
	public static final float ROLL_TIMER_SWITCH_TIMER = 0.10f;
	Animation<TextureRegion>[] rolls;
	
	float x = 0, y = 0;
	float stateTime;
	float rollTimer;
	float shootTimer;
	float asteroidSpawnTimer;
	float health = 1;
	// an integer roll to keep track of the roll of the sprite
	int roll;
	int score;
	
	Random random;
	
	ArrayList<Bullet> bullets;
	ArrayList<Asteroid> asteroids;
	ArrayList<Explosion> explosions;
	
	BitmapFont scoreFont;
	Texture blank;
	
	CollisionRect playerRect;
	
	Mathsplosive game;
	
	public GameScreen(Mathsplosive game) {
		this.game = game;
		y = 15;
		x = Mathsplosive.WIDTH / 2 - SHIP_WIDTH / 2;
		
		bullets = new ArrayList<Bullet>();
		asteroids = new ArrayList<Asteroid>();
		explosions = new ArrayList<Explosion>();
		
		// Get from internal file which is from assets folder
		scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
		shootTimer = 0;
		score = 0;
		
		playerRect = new CollisionRect(0, 0, SHIP_WIDTH, SHIP_HEIGHT);
		
		blank = new Texture("blank.png");
		// Generate number between 0.3 to 0.6 for the timer
		random = new Random();
		asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
		
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
		
		// Subtract delta from timer 
		asteroidSpawnTimer -= delta;
		
		// If less than 0 after being subtracted, spawn asteroid and reset timer
		if (asteroidSpawnTimer <= 0) {
			asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
			asteroids.add(new Asteroid(random.nextInt(Gdx.graphics.getWidth() - Asteroid.WIDTH)));
		}
		
		// Update asteroids
		ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
		
		for(Asteroid asteroid : asteroids) {
			asteroid.update(delta);
			
			if(asteroid.remove) {
				asteroidsToRemove.add(asteroid);
			}
		}
		
		// Update bullets
		ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
		
		for(Bullet bullet : bullets) {
			bullet.update(delta);
			
			if(bullet.remove) {
				bulletsToRemove.add(bullet);
			}
		}
		
		// Update explosions
		ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
		
		for (Explosion explosion : explosions) {
			explosion.update(delta);
			
			if(explosion.remove) {
				explosionsToRemove.add(explosion);
			}
		}
		
		explosions.removeAll(explosionsToRemove);
		
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
		
		playerRect.move(x, y);
		
		// Check if collide with asteroid
		for(Asteroid asteroid : asteroids ) {
			if(asteroid.getCollisionRect().collidesWith(playerRect)) {
				asteroidsToRemove.add(asteroid);
				health -= 0.1;
				
				if (health <= 0) {
					this.dispose();
					game.setScreen(new GameOverScreen(game, score));
				}
			}
		}
		
		for (Bullet bullet : bullets) {
			for (Asteroid asteroid : asteroids) {
				if(bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())) {
					bulletsToRemove.add(bullet);
					asteroidsToRemove.add(asteroid);
					explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
					score += 100;
				}
			}
		}
		
		asteroids.removeAll(asteroidsToRemove);
		bullets.removeAll(bulletsToRemove);
		
		stateTime += delta;
		// Runs render at every frame
		// OpenGL stuff
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Start drawing images to the screen
		game.batch.begin();
		
		// Need to pass string for second argument, but this is a workaround
		GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "" + score);
		
		// Set x to center, but y to 10 pixels below the top of the screen
		scoreFont.draw(game.batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - 10);
		
		for (Bullet bullet : bullets) {
			bullet.render(game.batch);
		}
		
		for (Asteroid asteroid : asteroids) {
			asteroid.render(game.batch);
		}
		
		for (Explosion explosion : explosions) {
			explosion.render(game.batch);
		}
		
		// Colouring health bar
		if(health > 0.6f) {
			game.batch.setColor(Color.GREEN);
		} else if(health > 0.2f) {
			game.batch.setColor(Color.ORANGE);
		} else {
			game.batch.setColor(Color.RED);
		}
		// Drawing the healthbar
		game.batch.draw(blank, 0, 0, Gdx.graphics.getWidth() * health, 5);
		
		game.batch.setColor(Color.WHITE);
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