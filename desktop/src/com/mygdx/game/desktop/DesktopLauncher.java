package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Mathsplosive;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		// Use foregroundFPS of config to change the fps lock
		config.foregroundFPS = 10;
		
		// Use width and height to control the size of the application window
		config.width = Mathsplosive.WIDTH;
		config.height = Mathsplosive.HEIGHT;
		
		config.resizable = false;
		new LwjglApplication(new Mathsplosive(), config);
	}
}
