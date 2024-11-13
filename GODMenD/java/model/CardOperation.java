package model;

import java.util.ArrayList;
import java.util.List;

public class CardOperation {
	private final int START_CARD = 4;
	private final int MAX_CARD = 8;
	
	//ゲーム開始時に引く
	public List<Card> StartDraw(List<Card> cardList){
		List<Card> handCardList = new ArrayList<>();

		for(int i = 0; i < START_CARD; i++) {
			int r = new java.util.Random().nextInt(cardList.size());
			handCardList.add(cardList.get(r));
		}
		
		return handCardList;
	}
	
	//カードを引く（消費した分ひく）
	public List<Card> Draw(List<Card> cardList, List<Card> handCardList, List<Integer> selectsInt){
		for(int i =0; i < selectsInt.size(); i++)
			if(handCardList.size() < MAX_CARD) {
				int r = new java.util.Random().nextInt(cardList.size());
				handCardList.add(cardList.get(r));
		}
		return handCardList;
	}
	
	//カードを引く（pass用)
	public List<Card> Draw(List<Card> cardList, List<Card> handCardList){
		if(handCardList.size() < MAX_CARD) {
			int r = new java.util.Random().nextInt(cardList.size());
			handCardList.add(cardList.get(r));
		} else {
			//もし、手持ちが8枚以上ならランダムで手札をすてて、1枚ひく
			int rDrop = new java.util.Random().nextInt(handCardList.size());
			handCardList.remove(rDrop);
			int r = new java.util.Random().nextInt(cardList.size());
			handCardList.add(rDrop, cardList.get(r));
		}
		return handCardList;
	}
	
	//カードをランダムで捨てる
	public List<Card> rDrop(List<Card> handCardList){
		int r = new java.util.Random().nextInt(handCardList.size());					
		handCardList.remove(r);
		return handCardList;
	}
	
	public List<Card> DropAndDrow(List<Card> cardList, List<Card> handCardList, List<Integer> selectsInt){
		for(int si : selectsInt) {
			handCardList.remove(si);
			int r = new java.util.Random().nextInt(cardList.size());
			handCardList.add(si, cardList.get(r));
		}
		
		return handCardList;
	}
	
	public List<Card> shuffle(List<Card> cardList, List<Card> handCardList){
		int handCard = handCardList.size();
		handCardList.clear();
		for(int i = 0; i < handCard; i++) {
			int r = new java.util.Random().nextInt(cardList.size());
			handCardList.add(cardList.get(r));
		}
		
		return handCardList;
	}

	public int getSTART_CARD() {
		return START_CARD;
	}

	public int getMAX_CARD() {
		return MAX_CARD;
	}
	
	

}
