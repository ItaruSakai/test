package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CPULogic {

	List<Integer> useCardNum = new ArrayList<Integer>(); //returnで返すリスト。何番目のカードを使用するか格納して返す。

	//攻撃、回復の時に何番目のカードを選ぶか決めるロジック。　戻り値はList。
	public List<Integer> attackCPULogic(List<Card> cpuCardList) {

		useCardNum.clear();
		List<Card> cardList = new ArrayList<Card>(cpuCardList); //現在持っているカードリストのインスタンス

		//何番目に何のカードが入っているか確認する。添え字をリストで管理。
		List<Integer> attackNum = new ArrayList<Integer>();
		List<Integer> healNum = new ArrayList<Integer>();

		//分類ごとにスイッチし、添え字を取得
		for (int i = 0; i < cardList.size(); i++) {
			switch (cardList.get(i).getCardType()) {
			case "攻撃":
				attackNum.add(i);
				break;
			case "回復":
				healNum.add(i);
				break;
			}
		}

		if (attackNum.size() != 0 && healNum.size() != 0) { //どちらもある場合は、ランダムに攻撃か回復を選ぶ。

			int r = new java.util.Random().nextInt(100) + 1; //1～100の数を生成

			//70％攻撃、30％回復（割合は仮）
			if (r <= 70) {
				useCardNum = attack(attackNum);
				return useCardNum;
			} else {
				useCardNum = heal(healNum);
				return useCardNum;
			}

		} else if (attackNum.size() != 0 && healNum.size() == 0) { //攻撃カードしかない時
			useCardNum = attack(attackNum);
			return useCardNum;

		} else if (attackNum.size() == 0 && healNum.size() != 0) { //回復カードしかない時
			useCardNum = heal(healNum);
			return useCardNum;

		}
		//どちらも0枚であれば、使えるカードはないので、リストをクリアにして返す
		useCardNum.clear();
		return useCardNum;
	}

	//防御の時に何番目のカードを選ぶか決めるロジック。　戻り値はList。
	public List<Integer> defenseCPULogic(List<Card> cpuCardList) {

		useCardNum.clear();
		List<Card> cardList = new ArrayList<Card>(cpuCardList); //現在持っているカードリストのインスタンス

		//何番目に何のカードが入っているか確認する。添え字をリストで管理。
		List<Integer> defenseNum = new ArrayList<Integer>();

		//分類ごとにスイッチし、添え字を取得
		for (int i = 0; i < cardList.size(); i++) {
			switch (cardList.get(i).getCardType()) {
			case "防御":
				defenseNum.add(i);
				break;
			}
		}

		if (defenseNum.size() != 0) { //防御カードがある場合は、
			useCardNum = defense(defenseNum);
			return useCardNum;
		}
		//どちらも0枚であれば、使えるカードはないので、リストをクリアにして返す
		useCardNum.clear();
		return useCardNum;
	}

	//攻撃
	public List<Integer> attack(List<Integer> attackNum) {

		//ランダムで選び、選んだカードをuseCardNumに格納。一枚しかなければそれが選ばれる
		int r = new java.util.Random().nextInt(attackNum.size());
		useCardNum.add(attackNum.get(r));

		return useCardNum;

	}

	//回復
	public List<Integer> heal(List<Integer> healNum) {

		//ランダムで選び、選んだカードをuseCardListに格納。一枚しかなければそれが選ばれる
		int r = new java.util.Random().nextInt(healNum.size());
		useCardNum.add(healNum.get(r));

		return useCardNum;
	}

	//防御
	public List<Integer> defense(List<Integer> defenseNum) {

		//防御カードが2枚以上ある場合は、ランダムで最大2枚まで使用
		if (defenseNum.size() >= 2) {
			//リストをシャッフル。実行の度に順番が変わる為、ランダムで取り出せる。
			Collections.shuffle(defenseNum);

			//2枚まで使用できるため、1枚使うときと2枚使うときの処理を分ける。割合は仮で50％。
			int r = new java.util.Random().nextInt(100) + 1; //1～100の数を生成

			if (r <= 50) {
				//2枚格納
				useCardNum.add(defenseNum.get(0));
				useCardNum.add(defenseNum.get(1));
			} else {
				//1枚格納
				useCardNum.add(defenseNum.get(0));
			}
		} else {
			//1枚しかない時は、1枚だけ格納
			useCardNum.add(defenseNum.get(0));
		}

		return useCardNum;
	}

}
