package com.mygdx.game.utils;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class InputHandler extends InputAdapter{
	String input = "";
	
	public String getInput() {
		return input;
	}

	public void resetString() {
		input = "";
	}

	public boolean keyDown(int keycode) {
		switch (keycode)   {
		case Keys.NUM_0:
			input += "0";
			break;
		case Keys.NUM_1:
			input += "1";
			break;
		case Keys.NUM_2:
			input += "2";
			break;
		case Keys.NUM_3:
			input += "3";
			break;
		case Keys.NUM_4:
			input += "4";
			break;
		case Keys.NUM_5:
			input += "5";
			break;
		case Keys.NUM_6:
			input += "6";
			break;
		case Keys.NUM_7:
			input += "7";
			break;
		case Keys.NUM_8:
			input += "8";
			break;
		case Keys.NUM_9:
			input += "9";
			break;
		case Keys.BACKSPACE:
		        input = input.substring(0, input.length() - 1);		    
			break;
	    }
		
	    return true;
	}
}
