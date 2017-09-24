package com.mygdx.game.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class Ship {

	public void moveAnimLeft() {
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
	}
	
	public void moveAnimRight() {
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			// Command to get the delta time, which is the time between rendered frames
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
	}
}
