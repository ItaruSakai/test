package model;

import java.io.Serializable;
import java.util.List;

public class Card implements Serializable {
	private String cardId;
	private String cardMei;
	private String cardType;
	private int attack;
	private int defense;
	private int heal;
	private String imgPath;
	private List<Card> selectPCard;
	private List<Card> selectCPUCard;

	public Card() {
	};

	public Card(String cardId, String cardMei, String cardType, int attack, int defense, int heal, String imgPath) {
		this.cardId = cardId;
		this.cardMei = cardMei;
		this.cardType = cardType;
		this.attack = attack;
		this.defense = defense;
		this.heal = heal;
		this.imgPath = imgPath;
	}

	public Card(List<Card> selectPCard, List<Card> selectCPUCard) {
		this.selectPCard = selectPCard;
		this.selectCPUCard = selectCPUCard;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardMei() {
		return cardMei;
	}

	public void setCardMei(String cardMei) {
		this.cardMei = cardMei;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getHeal() {
		return heal;
	}

	public void setHeal(int heal) {
		this.heal = heal;
	}
	
	

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public List<Card> getSelectPCard() {
		return selectPCard;
	}

	public void setSelectPCard(List<Card> selectPCard) {
		this.selectPCard = selectPCard;
	}

	public List<Card> getSelectCPUCard() {
		return selectCPUCard;
	}

	public void setSelectCPUCard(List<Card> selectCPUCard) {
		this.selectCPUCard = selectCPUCard;
	}

}
