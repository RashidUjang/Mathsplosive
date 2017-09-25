package com.mygdx.game.utils;

public class Question {
	private String question;
	private int answer;
			
	public Question(String question, int answer) {
		this.question = question;
		this.answer = answer;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public int getAnswer() {
		return answer;
	}
	
	public boolean checkAnswer(int answer) {
		return this.answer == answer;
	}
}
