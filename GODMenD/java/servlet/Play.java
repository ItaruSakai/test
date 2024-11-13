package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import model.BattleLogic;
import model.CPULogic;
import model.Card;
import model.CardOperation;
import model.MessageLogic;
import model.Player;
import model.Turn;
import model.User;


@WebServlet("/Play")
public class Play extends HttpServlet {
	private static final long serialVersionUID = 1L;
       


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//データベースからカードリストの情報を取得する
		DAO db = new DAO();
		List<Card> cardList = db.CardAll();
		CardOperation cardOperation = new CardOperation();	//カード操作
		Turn t = new Turn(1);														//ターン管理
		//プレイヤーとCPUのカードを取得する
		List<Card> playerCardList = cardOperation.StartDraw(cardList);
		List<Card> cpuCardList = cardOperation.StartDraw(cardList);
		//プレイヤーとCPUのインスタンス
		Player player = new Player();
		Player cpu = new Player();
		//CPUのカードリストを作成
		List<Card> selectCpuCardList = new ArrayList<Card>();
		CPULogic cpuLogic = new CPULogic();												//CPUのカード選択ロジック
		BattleLogic battleLogic = new BattleLogic();										//バトルロジック
		MessageLogic messageLogic = new MessageLogic();							//メッセージロジック
		List<String> msgList  = new ArrayList<String>();					//メッセージログ
		List<Integer> cpuCardNum = new ArrayList<Integer>();
		//セッションスコープ
		HttpSession session = request.getSession();
		//Userの情報をセッションスコープより取得
		User loginUser = (User)session.getAttribute("loginUser");
		int startHp = 50;																//初期HP
		int r = new java.util.Random().nextInt(100);						//乱数 (先攻、後攻を決める)
		if(r % 2 == 1 ) {																//プレイヤーが先攻　CPUが後攻
			player = new Player(1, startHp, playerCardList, loginUser.getName(),0,0);
			cpu = new Player(2, startHp, cpuCardList, "CPU" ,0 ,0);
		} else {																				//プレイヤーが後攻　CPUが先攻
			player = new Player(2, startHp, playerCardList, loginUser.getName(),0 ,0);
			cpu = new Player(1, startHp, cpuCardList, "CPU",0 ,0);
		}
		/*（チェック用) 先攻の場合は1 2 後攻の場合は2 1に入れ替えてください*/
//		player = new Player(2, startHp, playerCardList, loginUser.getName());
//		cpu = new Player(1, startHp, cpuCardList, "CPU");
		
		messageLogic.startMsg(player, msgList);								//先攻、後攻のメッセージを表示
		
		if(cpu.getNo() == 1) {									//CPUが先攻だった場合、CPUに攻撃カードを選択させる
			/*CPUに攻撃カードを選択させる*/ 
			cpuCardNum = cpuLogic.attackCPULogic(cpu.getHandCardList());
			for(int ccn : cpuCardNum) {
				selectCpuCardList.add(cpu.getHandCardList().get(ccn));
			}
			if(selectCpuCardList.isEmpty() == false) { 													//CPUがカードを選択した場合
				messageLogic.selectCardMsg(cpu, selectCpuCardList, msgList);						//選択したカードをメッセージに表示する
				if(selectCpuCardList.get(0).getCardType().equals("攻撃")) {	//攻撃カードの場合
					messageLogic.dTurnMsg(player, msgList);							//プレイヤーに防御カードを選択する旨を伝えるメッセージを表示
				} else { 		//回復カードの場合
					//CPUがカードを消費し、消費した分カードを引く (消費しなければスキップされる)
					cpu.setHandCardList(cardOperation.DropAndDrow(cardList, cpu.getHandCardList(), cpuCardNum));
					battleLogic.heal(cpu, selectCpuCardList.get(0), cardList, msgList);			//回復だけするロジックを呼ぶ
					
					//ターンをすすめる
					t.setTurn(t.getTurn() + 1);
					messageLogic.aTurnMsg(player, msgList);														//攻撃カードを選択する旨を伝えるメッセージを表示する
				}
			} else { 			//CPUがパスを選んだ場合はカードを1枚引く
				cpu.setHandCardList(cardOperation.Draw(cardList, cpu.getHandCardList()));	//カードを1枚引く
				messageLogic.pass(cpu, cardOperation.getMAX_CARD(), msgList);
				//ターンをすすめる
				t.setTurn(t.getTurn() + 1);
			}
		}
		
		//データベースから取得した全カード情報を保存する
		session.setAttribute("cardList", cardList);
		//セッションスコープにCPUのカード情報を保存する
		session.setAttribute("selectCpuCardList", selectCpuCardList);
		session.setAttribute("cpuCardNum", cpuCardNum);
		//ターン情報を保存
		session.setAttribute("turn", t);
		//プレイヤー情報を保存
		session.setAttribute("player", player);
		session.setAttribute("cpu", cpu);
		//メッセージ情報を保存する
		session.setAttribute("msgList", msgList);
		
		//play画面にフォワードする
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/play.jsp");
		dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String[] selects= request.getParameterValues("select");						//jspから選んだカード番号を受け取る
	
		List<Integer> playerCardNum = new ArrayList<Integer>();						//番号情報を保存するリスト
		if(selects != null) {
			for(String s : selects) {																		//String→Integerのリストに変換する
				playerCardNum.add(Integer.parseInt(s));
			} 
		}
		HttpSession session = request.getSession();										//セッションスコープ
		List<Card> cardList = (List<Card>)session.getAttribute("cardList");	//全カード情報を取得する
		Turn t = (Turn)session.getAttribute("turn");										//現在のターンを取得する
		Player player = (Player)session.getAttribute("player");						//プレイヤーの情報を取得する
		Player cpu = (Player)session.getAttribute("cpu");								//CPUの情報を取得する
		List<Integer> cpuCardNum = (List<Integer>)session.getAttribute("cpuCardNum");	
		List<Card> selectCpuCardList = (List<Card>)session.getAttribute("selectCpuCardList");		//CPU側が選択したカードの情報を取得する
		List<String> msgList = (List<String>)session.getAttribute("msgList");	//メッセージの情報を取得する
		
		List<Card> selectPlayerCardList = new ArrayList<Card>();				//プレイヤーが選択したカードのリスト
		CardOperation cardOperation = new CardOperation();						//カード操作（カードを引く、カードを捨てる)のロジック
		CPULogic cpuLogic = new CPULogic();												//CPUのカード選択ロジック
		MessageLogic messageLogic = new MessageLogic();							//メッセージロジック
		BattleLogic battleLogic = new BattleLogic();										//バトルロジック
		
		/* ターンとCPUの手持ちカード確認用*/
//		System.out.println(t.getTurn());
//		for(Card c : cpu.getHandCardList()) {
//			System.out.print(c.getCardMei() + " ");
//		} 
		
		
		if((player.getNo() + t.getTurn()) % 2 == 0){					//プレイヤーが攻撃側だった時の処理
			if(playerCardNum.isEmpty() == false) {													//いずれかのカードが選ばれたら
				//カード番号を参照し、プレイヤー側の攻撃カードを決定する
				for(int pcn : playerCardNum) {
					selectPlayerCardList.add(player.getHandCardList().get(pcn));			//チェックボックスで選択された分のカードをリストに保存する
				}
				messageLogic.selectCardMsg(player, selectPlayerCardList, msgList); //選択したカードをメッセージに表示する
				if(selectPlayerCardList.get(0).getCardType().equals("攻撃")) { 	//攻撃カードが選択されていたら
					/*CPUに防御カードを選択させる*/ 
					selectCpuCardList.clear();
					cpuCardNum = cpuLogic.defenseCPULogic(cpu.getHandCardList());
					if(cpuCardNum.isEmpty() == false) {
						for(int ccn : cpuCardNum) {
							selectCpuCardList.add(cpu.getHandCardList().get(ccn));
						}
						messageLogic.selectCardMsg(cpu, selectCpuCardList, msgList);			//CPUの選択したカードをメッセージに表示
					} else {
						messageLogic.selectCardMsg(cpu, selectCpuCardList, msgList);			//CPUの選択したカードをメッセージに表示
						selectCpuCardList.add(new Card("0000", "0", "防御", 0, 0, 0, "0"));				//防御0の値を入れる
					}
					
					//プレイヤーがカードを消費し、消費した分カードを引く
					player.setHandCardList(cardOperation.DropAndDrow(cardList, player.getHandCardList(), playerCardNum));
					//バトル処理
					battleLogic.battle(player, cpu, selectPlayerCardList, selectCpuCardList, msgList);
					
					
				} else {		//回復カードが選択されていたら
					//プレイヤーがカードを消費し、消費した分カードを引く
					player.setHandCardList(cardOperation.DropAndDrow(cardList, player.getHandCardList(), playerCardNum));
					//回復だけするロジックを呼ぶ
					battleLogic.heal(player, selectPlayerCardList.get(0), cardList, msgList);
					cpuCardNum.clear();
				} 
				
				//CPUがカードを消費し、消費した分カードを引く (消費しなければスキップされる)
				if(cpuCardNum.isEmpty() == false) {
					cpu.setHandCardList(cardOperation.DropAndDrow(cardList, cpu.getHandCardList(), cpuCardNum));
				}
				
			} else {					// カードが選ばれなかった(Pass時の処理)
				player.setHandCardList(cardOperation.Draw(cardList, player.getHandCardList())); //1枚引く
				messageLogic.pass(player, cardOperation.getMAX_CARD(), msgList);					//パスのメッセージを表示
			}
			
		} else {						//プレイヤーが防御側だったときの処理
			if(playerCardNum.isEmpty() == false) {																	//プレイヤーがいずれかのカードを選んでいたら
				//カード番号を参照し、プレイヤー側の防御カードが決定する
				for(int pcn : playerCardNum) {
					selectPlayerCardList.add(player.getHandCardList().get(pcn));			//チェックボックスで選択された分のデータをpCardsのリストに保存する
				}
				messageLogic.selectCardMsg(player, selectPlayerCardList, msgList);				//選択したカードをメッセージに表示
				//プレイヤーがカードを消費し、消費した分カードを引く
				player.setHandCardList(cardOperation.DropAndDrow(cardList, player.getHandCardList(), playerCardNum));
				//バトル処理 ※CPUの選択したカードはセッションスコープに保存しているので、そちらを使う
				battleLogic.battle(player, cpu, selectPlayerCardList, selectCpuCardList, msgList);
			} else {					//カードが選択されなければ
				messageLogic.selectCardMsg(player, selectPlayerCardList, msgList);				//選択したカードをメッセージに表示(選択されなかった場合のメッセージを表示する)
				selectPlayerCardList.add(new Card("0000", "0", "防御", 0, 0, 0, "0"));				//防御0の値を入れる
				//バトル処理 ※CPUの選択したカードはセッションスコープに保存しているので、そちらを使う
				battleLogic.battle(player, cpu, selectPlayerCardList, selectCpuCardList, msgList);
			}
			//CPUがカードを消費し、消費した分カードを引く
			cpu.setHandCardList(cardOperation.DropAndDrow(cardList, cpu.getHandCardList(), cpuCardNum)); 
		}
		
		//勝敗判定
		if(player.getHp() <= 0) {		//プレイヤーのHPが0になったら、false(負け)
			boolean result = false;
			messageLogic.resultHpMsg(player, cpu, msgList);
			request.setAttribute("result", result);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/Result"); //←Resultサーブレットにフォワードする
			dispatcher.forward(request, response);
			return;
		}
		if(cpu.getHp() <= 0) {			//CPUのHPが0になったら、true(勝ち)
			boolean result = true;
			messageLogic.resultHpMsg(player, cpu, msgList);
			request.setAttribute("result", result);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/Result");//←Resultサーブレットにフォワードする
			dispatcher.forward(request, response);
			return;
		}
		
		//ターンをすすめる
		t.setTurn(t.getTurn() + 1);
		//CPU側が選択したカード情報を初期化
		selectCpuCardList = new ArrayList<Card>(); 
		
			
		if((player.getNo() + t.getTurn()) % 2 == 1){									//プレイヤーが防御側の処理
			/*CPUに攻撃カードを選択させる*/ 
			cpuCardNum = cpuLogic.attackCPULogic(cpu.getHandCardList());
			for(int ccn : cpuCardNum) {
				selectCpuCardList.add(cpu.getHandCardList().get(ccn));
			}
			if(cpuCardNum.isEmpty() == false) { 													//CPUがカードを選択した場合
				messageLogic.selectCardMsg(cpu, selectCpuCardList, msgList);				//CPUの選択したカードを表示する
				if(selectCpuCardList.get(0).getCardType().equals("攻撃")) {	//攻撃カードの場合
					messageLogic.dTurnMsg(player, msgList);													//防御カードを選択する旨を伝えるメッセージを表示する
				} else { 		//回復カードの場合
					//CPUがカードを消費し、消費した分カードを引く (消費しなければスキップされる)
					cpu.setHandCardList(cardOperation.DropAndDrow(cardList, cpu.getHandCardList(), cpuCardNum));
					//回復だけするロジックを呼ぶ
					battleLogic.heal(cpu, selectCpuCardList.get(0), cardList, msgList);	
					//ターンをすすめる
					t.setTurn(t.getTurn() + 1);
					messageLogic.aTurnMsg(player, msgList);													//攻撃カードを選択する旨を伝えるメッセージを表示する
				}
			} else { 			//CPUがパスを選んだ場合はカードを1枚引く
				cpu.setHandCardList(cardOperation.Draw(cardList, cpu.getHandCardList())); //カードを1枚引く
				messageLogic.pass(cpu, cardOperation.getMAX_CARD(), msgList);
				//ターンをすすめる
				t.setTurn(t.getTurn() + 1);
				messageLogic.aTurnMsg(player, msgList);														//攻撃カードを選択する旨を伝えるメッセージを表示する
			}
		} else {
			messageLogic.aTurnMsg(player, msgList);														//攻撃カードを選択する旨を伝えるメッセージを表示する
		}
		
		//セッションスコープにCPUのカード情報を保存する
		session.setAttribute("cpuCardNum", cpuCardNum);
		session.setAttribute("selectCpuCardList", selectCpuCardList);
		//セッションスコープにメッセージ情報を更新する
		session.setAttribute("msgList", msgList);
		//セッションスコープにプレイヤーとCPUの情報を更新する
		session.setAttribute("player", player);
		session.setAttribute("cpu", cpu);
		//ターン情報を保存
		session.setAttribute("turn", t);
		
		//play画面にフォワードする
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/play.jsp");
		dispatcher.forward(request, response);
	}
}
