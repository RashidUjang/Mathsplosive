package com.mygdx.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.utils.CollisionRectangle;

public class Bullet extends GameObject{
	public static final int SPEED = 1500;
	
	// 40 because the bullet is from the ship
	public static final int COORDINATE = 40;
	public static final int WIDTH = 18;
	public static final int HEIGHT = 17;
	
	// Static because every bullet accesses the same texture 
	private static Texture texture;
	
	int rotation;
	CollisionRectangle rect;
	
	public Bullet(float x, int rotation) {
		this.x = x;
		this.y = COORDINATE;
		this.rotation = rotation;
		
		this.rect = new CollisionRectangle(x, y, WIDTH, HEIGHT);
		
		if(texture == null) {
			texture = new Texture("bullet.png");
		}
	}
	
	public void update(float deltaTime) {
		float dx = (float) (SPEED * (Math.sin(Math.toRadians(rotation))) * deltaTime);
		x -= dx;
		y += (float) Math.sqrt(Math.pow(SPEED, 2) - Math.pow(Math.abs(dx / deltaTime), 2)) * deltaTime;
		// remove bullet if it is more than screen height
		if (y > Gdx.graphics.getHeight() || x > Gdx.graphics.getWidth()) {
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
}