package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.utils.ScrollingBackground;

public class Mathsplosive extends Game {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.scrollingBackground = new ScrollingBackground();
		this.setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void render () {
		super.render();
	}
	
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
	
	public void resize(int width, int height) {
		this.scrollingBackground.resize(width, height);
		super.resize(width, height);
	}
}