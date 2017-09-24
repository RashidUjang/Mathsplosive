package com.mygdx.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.utils.CollisionRect;

public class Asteroid implements Comparable<Asteroid>{
	public static final int SPEED = 25;
	
	// 16 is width of asteroid
	public static final int WIDTH = 16;
	public static final int HEIGHT = 16;
	
	// Static because every bullet accesses the same texture 
	private static Texture texture;
	// x coordinate and y coordinate of the object
	float x, y;
	
	CollisionRect rect;
	
	// Check if object should be removed
	public boolean remove = false;
	
	public Asteroid(float x) {
		this.x = x;
		this.y = Gdx.graphics.getHeight();
		
		this.rect = new CollisionRect(x, y, WIDTH, HEIGHT);
		
		if(texture == null) {
			texture = new Texture("asteroid.png");
		}
	}
	
	public void update(float deltaTime) {
		y -= SPEED * deltaTime;
		
		// remove asteroid if it is less than screen height
		if (y < -HEIGHT) {
			remove = true;
		}
		
		rect.move(x, y);
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, x, y);
	}
	
	public CollisionRect getCollisionRect() {
		return rect;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public int compareTo(Asteroid other)
	{
		if (x < other.getX()) {
			
			return -1;
		}
		
		if (x > other.getX()) {
			
			return 1;
		}
		
		return 0;
	}
}
