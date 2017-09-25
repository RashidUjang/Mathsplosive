package com.mygdx.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.utils.CollisionRectangle;

public class Asteroid extends GameObject {
	public static final int SPEED = 50;
	
	// 16 is width of asteroid
	public static final int WIDTH = 128;
	public static final int HEIGHT = 128;
	
	// Static because every bullet accesses the same texture 
	private static Texture texture;
	
	CollisionRectangle rect;
	
	public Asteroid(float x) {
		this.x = x;
		this.y = Gdx.graphics.getHeight();
		
		this.rect = new CollisionRectangle(x, y, WIDTH, HEIGHT);
		
		if(texture == null) {
			texture = new Texture("asteroid.png");
		}
	}
	
	public void update(float deltaTime, float difficultyModifier) {
		y -= SPEED * deltaTime * difficultyModifier;
		
		// remove asteroid if it is less than screen height
		if (y < -HEIGHT) {
			remove = true;
		}
		
		rect.move(x, y);
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, x, y);
	}
	
	public CollisionRectangle getCollisionRect() {
		return rect;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public boolean equals(Asteroid otherAsteroid) {
		return (otherAsteroid.getX() == x) && (otherAsteroid.getY() == y);
	}
}
