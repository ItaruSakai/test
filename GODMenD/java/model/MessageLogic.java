package model;

import java.util.List;

public class MessageLogic {
	
	public void damageMsg(Player aPlayer, Player dPlayer, int damage, List<String> msgList) {
		if(damage == 0) {
			msgList.add(dPlayer.getName() + "の防御が固い!!    " + aPlayer.getName() + "はダメージを与えられなかった");
		}	else {
			msgList.add(aPlayer.getName() + "の攻撃!!     " + dPlayer.getName() + "は " + damage + " のダメージを受けた" );
		}
	}
	
	public void selectCardMsg(Player p, List<Card> selectCardList, List<String> msgList) {
		if(selectCardList.isEmpty()) {
			if(p.getGuard() > 0) {
				msgList.add(p.getName() +  "は防御カードを選択しなかった    " +p.getName() +  "の防御力>> " + p.getGuard());
			} else {
				msgList.add(p.getName() +  "は防御カードを選択しなかった    " + p.getName() +  "の防御力>>0 ");
			}
		} else {
			Card card = selectCardList.get(0);								//1枚目のカードをインスタンスに保存
			switch(card.getCardType()) {
				case "攻撃":
					if(p.getName().equals("CPU")) {
						msgList.add("CPUのターン-------------------------");
					}
					if(p.getPower() > 0) {
						msgList.add(p.getName() + "は" + card.getCardMei() + "のカードを選択 >> 攻撃力:" + card.getAttack() + " + " + p.getPower());
					} else {
						msgList.add(p.getName() + "は" + card.getCardMei() + "のカードを選択 >> 攻撃力:" + (card.getAttack()));
					}
					
					break;
				case "防御":
					String msg = "";
					int msgNum = 1;
					int defense = 0;
					for(Card c : selectCardList) {
						msg += c.getCardMei();
						defense += c.getDefense();
						if(selectCardList.size() != msgNum) {
							msg += ",";
						}
						msgNum ++;
					}
					if(p.getGuard() > 0) {
						msgList.add(p.getName() + "は" + msg + "のカードを選択 >> 防御力:" + defense + " + " + p.getGuard());
					} else {
						msgList.add(p.getName() + "は" + msg + "のカードを選択 >> 防御力:" + defense);
					}
					
					break;
				case "回復":
					if(p.getName().equals("CPU")) {
						msgList.add("CPUのターン-------------------------");
					}
					msgList.add(p.getName() + "は" + card.getCardMei() + "を選択   " + "HPを " + card.getHeal() + " 回復!!");
					break;
			}
		}		
	}
	
	
	public void pass(Player p, int maxCard, List<String> msgList) {
		if(p.getHandCardList().size() < maxCard) {
			msgList.add(p.getName() + "はパスを選択　カードを1枚引いた");
		} else {
			msgList.add(p.getName() + "はパスを選択　これ以上持てないので、ランダムで1枚捨て、1枚引いた");
		}
	}
	
	public void aTurnMsg(Player p, List<String> msgList) {
		msgList.add(p.getName() + "のターン-------------------");
		msgList.add("攻撃、回復カードを選択してください");
	}
	
	public void  dTurnMsg(Player p, List<String> msgList) {
		msgList.add(p.getName() + "は防御カードを選択してください");
	}
	
	public void startMsg(Player p, List<String> msgList) {
		if(p.getNo() == 1) {
			msgList.add(p.getName() + "が先攻です");
			msgList.add("攻撃カードを選択してください");
		} else {
			msgList.add(p.getName() + "が後攻です");
		}
	}
	
	public void defeatMsg(Player p, List<String> msgList) {
		
		switch(p.getName()) {
			case "CPU" :
				msgList.add(p.getName() + "を倒した！！");
				break;
			default :
				msgList.add(p.getName() + "は倒れてしまった");
		}
		
	}
	public void resultHpMsg(Player player, Player cpu, List<String> msgList) {
		msgList.add("<残りHP>  " + player.getName() + " HP:" + player.getHp() + "    " + cpu.getName() + " HP:" + cpu.getHp());
	}

}
