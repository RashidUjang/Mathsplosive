package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
	public static final int SPEED = 500;
	
	// 40 because the bullet is from the ship
	public static final int DEFAULT_Y = 40;
	
	// Static because every bullet accesses the same texture 
	private static Texture texture;
	
	float x, y;
	
	// Check if object should be removed
	public boolean remove = false;
	
	public Bullet(float x) {
		this.x = x;
		this.y = DEFAULT_Y;
		
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
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, x, y);
	}
}