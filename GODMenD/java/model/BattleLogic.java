package model;

import java.util.List;

public class BattleLogic {

	public void battle(Player player, Player cpu, List<Card> selectPCard, List<Card> selectCPUCard, List<String> msgList) { //引数の型や名前は仮

		Card card = new Card(selectPCard, selectCPUCard); //リストを渡し、インスタンス生成
		MessageLogic messageLogic = new MessageLogic();//メッセージ表示のロジック
		
		Card card1 = card.getSelectPCard().get(0);  //プレイヤー側の選択カードリストの1番目のカード情報を取り出す
		Card card2 = card.getSelectCPUCard().get(0); //プレイヤー側が防御の時に使う
		
		
		//プレイヤーのカードタイプによって処理を分ける(基本的には同じ種類がリストに入っているため、見るのは先頭のカードだけでOK!)
		switch (card1.getCardType()) {
		case "攻撃":
			//総合防御力
			int defense1 = 0;
			//CPUの防御力を加算する
			defense1 += cpu.getGuard();
			
			//防御カードは複数選択されている可能性がある為、全部取り出して各防御力を足していく
			for (Card c1 : selectCPUCard) {
				defense1 += c1.getDefense();
			}
			
			//ダメージ計算
			int damage1 = card1.getAttack() + player.getPower() - defense1;
			
			//ダメージが0以下の場合はダメージ0、ダメージが1以上の場合は防御側（CPU）のHPからダメージ分を引く
			if (damage1 <= 0) {
				damage1 = 0;
			} else {
				cpu.setHp(cpu.getHp() - damage1);
			}
			messageLogic.damageMsg(player, cpu, damage1, msgList);
			//CPUのHPが0になったとき（０以下になったとき）
			if(cpu.getHp() <= 0) {
				cpu.setHp(0);
				messageLogic.defeatMsg(cpu, msgList);
			}
			break;
		case "回復":
			//カードが持つ回復量をプレイヤーのHPに足してセット
			player.setHp(player.getHp() + card1.getHeal());
			break;
		case "防御":
			//総合防御力
			int defense2 = 0;
			//プレイヤーの防御力を加算する
			defense2 += player.getGuard();
			
			//プレイヤーが選択した防御カードの防御力を全部足す
			for (Card c2 : selectPCard) {
				defense2 += c2.getDefense();
			}
			
			//ダメージ計算
			int damage2 = card2.getAttack() + cpu.getPower() - defense2;

			//ダメージが0以下の場合はダメージ0、ダメージが1以上の場合は防御側（プレイヤー）のHPからダメージ分を引く
			if (damage2 <= 0) {
				damage2 = 0;
			} else {
				player.setHp(player.getHp() - damage2);
			}
			messageLogic.damageMsg(cpu, player, damage2, msgList);
			//プレイヤーのHPが0になったとき（０以下になったとき）
			if(player.getHp() <= 0) {
				player.setHp(0);
				messageLogic.defeatMsg(player, msgList);
			}
			break;

		}

	}
	
	public void heal(Player player, Card healCard, List<Card> cardList, List<String> msgList) {
		CardOperation cardOperation = new CardOperation();		
		//カードが持つ回復量をプレイヤー(CPU)のHPに足してセット
		player.setHp(player.getHp() + healCard.getHeal());
		switch(healCard.getCardId()) {
			case "1001" :	//回復しつつ、手札をドローする
				player.setHandCardList(cardOperation.Draw(cardList, player.getHandCardList()));
				msgList.add(healCard.getCardMei() + "の効果でカードを1枚引いた");
				break;
			case "1002" :	//手札を1枚捨てる
				player.setHandCardList(cardOperation.rDrop(player.getHandCardList()));
				msgList.add(healCard.getCardMei() + "の効果でカードを1枚捨てた");
				break;
			case "1003":		//攻撃力を上げる
				int powerUp = 5;
				if(player.getPower() < 30) {
					int power = player.getPower() + powerUp;
					player.setPower(power);
					msgList.add(healCard.getCardMei() + "の効果で、" + player.getName() + "の攻撃力が " + powerUp + " 上がった" );
				} else {
					msgList.add("これ以上、攻撃力は上がらないようだ");
				}
				break;
			case "1004":		//防御力を上げる
				int guardUp = 5;
				if(player.getGuard() < 20) {
					int guard = player.getGuard() + guardUp;
					player.setGuard(guard);
					msgList.add(healCard.getCardMei() + " の効果で、" + player.getName() + " の防御力が " + guardUp + " 上がった" );
				} else {
					msgList.add("これ以上、防御力は上がらないようだ");
				}
				break;
			case "1005":		//すべての手札を引き直す
				player.setHandCardList(cardOperation.shuffle(cardList, player.getHandCardList()));
				msgList.add("すべてのカードを引き直した！！" );
				break;
		}
	}

}
