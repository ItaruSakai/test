package model;

import java.io.Serializable;

public class Turn implements Serializable {
	private int turn;
	
	public Turn() {}
	
	public Turn(int turn){
		this.turn = turn;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	
}
