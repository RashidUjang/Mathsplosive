package com.mygdx.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.utils.CollisionRect;

public class Bullet {
	public static final int SPEED = 1500;
	
	// 40 because the bullet is from the ship
	public static final int DEFAULT_Y = 40;
	public static final int WIDTH = 3;
	public static final int HEIGHT = 12;
	
	// Static because every bullet accesses the same texture 
	private static Texture texture;
	
	float x, y;
	
	CollisionRect rect;

	// Check if object should be removed
	public boolean remove = false;
	
	public Bullet(float x) {
		this.x = x;
		this.y = DEFAULT_Y;
		
		this.rect = new CollisionRect(x, y, WIDTH, HEIGHT);
		
		if(texture == null) {
			texture = new Texture("bullet.png");
		}
	}
	
	public void update(float deltaTime) {
		y += SPEED * deltaTime;
		
		// remove bullet if it is more than screen height
		if (y > Gdx.graphics.getHeight()) {
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
}