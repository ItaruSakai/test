package model;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {
	private int no;
	private int hp;
	private List<Card> handCardList;
	private String name;
	private int power;
	private int guard;
	
	public Player(){}
	
	public Player(int no, int hp, List<Card> handCardList, String name, int power, int guard) {
		this.no = no;
		this.hp = hp;
		this.handCardList = handCardList;
		this.name = name;
		this.power = power;
		this.guard = guard;
	}
	

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public List<Card> getpCardList() {
		return handCardList;
	}

	public void setpCardList(List<Card> handCardList) {
		this.handCardList = handCardList;
	}

	public List<Card> getHandCardList() {
		return handCardList;
	}

	public void setHandCardList(List<Card> handCardList) {
		this.handCardList = handCardList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getGuard() {
		return guard;
	}

	public void setGuard(int guard) {
		this.guard = guard;
	}
	
	

	
	
	

}
