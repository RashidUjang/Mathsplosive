package com.mygdx.game.screens;

import java.util.ArrayList;
import java.util.Collections;
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
import com.mygdx.game.gameobjects.Asteroid;
import com.mygdx.game.gameobjects.Bullet;
import com.mygdx.game.gameobjects.Explosion;
import com.mygdx.game.gameobjects.GameObject;
import com.mygdx.game.utils.CollisionRectangle;
import com.mygdx.game.utils.InputHandler;
import com.mygdx.game.utils.Question;
import com.mygdx.game.utils.QuestionGenerator;

public class GameScreen implements Screen {
	
	// Speed for movement of image. 40 pixels per second
	public static final float SPEED = 300;
	public static final int SHIP_WIDTH_PIXEL = 17;
	public static final int SHIP_HEIGHT_PIXEL = 32;
	// 17 * 3, because the spritesheet for ship is 17
	public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
	// 32 * 3, because the spritesheet for ship is 32
	public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;
	// Animation speed. 0.5 secs per frame
	public static final float SHIP_ANIMATION_SPEED = 0.5f;
	public static final float MIN_ASTEROID_SPAWN_TIME = 2.5f;
	public static final float MAX_ASTEROID_SPAWN_TIME = 4.5f;
	// 0.15 seconds per switch to next animation
	public static final float ROLL_TIMER_SWITCH_TIMER = 0.10f;
	Animation<TextureRegion>[] rolls;
	
	float x; 
	float y;
	float stateTime;
	float rollTimer;
	float asteroidSpawnTimer;
	float health = 1;
	float difficultyTimer;
	float difficultyModifier;
	// an integer roll to keep track of the roll of the sprite
	int roll;
	int input;
	int score;
	int rotation;
	
	Random random;
	
	ArrayList<Bullet> bullets;
	ArrayList<Asteroid> asteroids;
	ArrayList<Explosion> explosions;
	
	BitmapFont scoreFont;
	Texture blank;
	CollisionRectangle playerRect;
	Asteroid currentTarget;
	Question question;
	QuestionGenerator questionGen;
	InputHandler inputHandler;
	
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
		score = 0;
		rotation = 0;
		currentTarget = null;
		
		questionGen = new QuestionGenerator();
		question = questionGen.generateQuestion();
		inputHandler = new InputHandler();
		difficultyModifier = 1.0f;
		playerRect = new CollisionRectangle(0, 0, SHIP_WIDTH, SHIP_HEIGHT);
		
		blank = new Texture("blank.png");
		// Generate number between 0.3 to 0.6 for the timer
		random = new Random();
		asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
		
		difficultyTimer = 5;
		
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
	
		game.scrollingBackground.setSpeedFixed(false);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputHandler);
	}

	@Override
	public void render(float delta) {
		// Subtract delta from timer 
		difficultyTimer += delta;
		
		if(difficultyTimer <= 0) {
			difficultyModifier *= 1.1;
			difficultyTimer = 5;
			questionGen.updateDifficulty(difficultyModifier);
		}
			
		asteroidSpawnTimer -= delta * difficultyModifier;
				
		// If less than 0 after being subtracted, spawn asteroid and reset timer
		if (asteroidSpawnTimer <= 0) {
			asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
			asteroids.add(new Asteroid(random.nextInt(Gdx.graphics.getWidth() - Asteroid.WIDTH)));
		}
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			rotation += 2;
			rotation %= 360;
		}
		
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			rotation -= 2;
			rotation %= 360;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				// Check if question is answered correctly
			if(inputHandler.getInput() != "") {
				input = Integer.parseInt(inputHandler.getInput());
			}
				
			if (question.checkAnswer(input)) {
				bullets.add(new Bullet(x + SHIP_WIDTH / 2, rotation));
				question = questionGen.generateQuestion();
			}
			
			inputHandler.resetString();
		}
	
		// Update asteroids
		ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
		
		for(Asteroid asteroid : asteroids) {
			asteroid.update(delta, difficultyModifier);
			
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
		
		// isKeyPressed means is it pressed at that instant
		// if isKeyJustPressed means that is it just recently pressed last frame
		
		// Check if collide with asteroid
		for(Asteroid asteroid : asteroids ) {
			if(asteroid.getY() < y) {
				asteroidsToRemove.add(asteroid);
				health -= 0.1;
				
				if (health <= 0) {
					this.dispose();
					game.setScreen(new GameOverScreen(game, score));
				}
			}
		}
		
		// Check if collide with bullet
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
		
		explosions.removeAll(explosionsToRemove);
		asteroids.removeAll(asteroidsToRemove);
		bullets.removeAll(bulletsToRemove);
		
		stateTime += delta;
		// Runs render at every frame
		// OpenGL stuff
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Start drawing images to the screen
		game.batch.begin();
		
		game.scrollingBackground.updateAndRender(delta, game.batch);
		// Need to pass string for second argument, but this is a workaround
		GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: " + score);
		GlyphLayout questionLayout = new GlyphLayout(scoreFont, "" + question.getQuestion() + " [" + input + "]");
		
		// Set x to center, but y to 10 pixels below the top of the screen
		
		for (Bullet bullet : bullets) {
			bullet.render(game.batch);
		}
		
		for (Asteroid asteroid : asteroids) {
			asteroid.render(game.batch);
		}
		
		for (Explosion explosion : explosions) {
			explosion.render(game.batch);
		}
		
		scoreFont.draw(game.batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - 10);
		scoreFont.draw(game.batch, questionLayout, Gdx.graphics.getWidth() / 2 - questionLayout.width / 2, Gdx.graphics.getHeight() / 2);
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
		// Draw with rotation
		game.batch.draw(rolls[roll].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH / 2, SHIP_HEIGHT / 2, SHIP_WIDTH, SHIP_HEIGHT, 1, 1, rotation);
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