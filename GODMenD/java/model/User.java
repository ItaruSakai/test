package model;

import java.io.Serializable;

public class User implements Serializable{
	private String name;
	private String pass;
	private int win;
	private int lose;
	
	public User(String name, String pass) {
		this.name = name;
		this.pass = pass;

	}
	
	public User(int win, int lose) {
		this.win = win;
		this.lose = lose;
	}

	public String getName() {
		return name;
	}

	public String getPass() {
		return pass;
	}

	public int getWin() {
		return win;
	}

	public int getLose() {
		return lose;
	}
	
	
}
