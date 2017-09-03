package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainMenuScreen;

public class Mathsplosive extends Game {
	
	public static final int WIDTH = 480;
	public static final int HEIGHT = 720;
	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void render () {
		super.render();
	}
	
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
}
