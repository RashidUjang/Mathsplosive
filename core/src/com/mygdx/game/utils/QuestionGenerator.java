package com.mygdx.game.utils;

import java.util.Random;

public class QuestionGenerator {

	Random random = new Random();

	private int maxAnswer = 30;
	private int operand1 = 0;
	private int operand2 = 0;

	public Question generateQuestion() {
		int answer = random.nextInt(maxAnswer);
		int operator = random.nextInt(2);
		
		if(operator == 0) {
			operand1 = random.nextInt(answer);
			operand2 = answer - operand1;
		} else if(operator == 1) {
			operand1 = random.nextInt(answer) + answer;
			operand2 = operand1 - answer;
		}
		
		return new Question(buildString(operand1, operand2, operator), answer);
	}

	private String buildString(int operand1, int operand2, int operator) {
		String operators = "+-*/";

		return operand1 + " " + operators.charAt(operator) + " " + operand2 + " = ?";
	}
	
	public void updateDifficulty(float difficultyModifier) {
		maxAnswer *= difficultyModifier;
	}
}