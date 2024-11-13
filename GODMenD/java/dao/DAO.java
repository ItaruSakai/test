package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.User;

public class DAO {
	//データベース接続に使用する情報
	private final String URL = "jdbc:postgresql://localhost:5432/godmend";
	private final String USER = "postgres";
	private final String PASSWORD = "test";

	//コンストラクタ
	public DAO() {
		//JDBCドライバを読み込む
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//ユーザー情報を一件追加
	public boolean AddUser(User user) {
		//SQL文の準備
		String sql = "INSERT INTO Login VALUES (?, ?, 0, 0);";

		//PostgreSQLへの接続
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement st = con.prepareStatement(sql);) {

			//SQL文の?部分を置き換え
			st.setString(1, user.getName());
			st.setString(2, user.getPass());

			//SQL文の実行
			st.executeUpdate();

			return true;

		} catch (Exception e) {
			System.out.println("DBアクセス時にエラーが発生しました。");
			e.printStackTrace();
			return false;
		}
	}

	//ユーザー名が被っていないか確認
	public boolean DuplicateUser(User user) {
		//SQL文の準備
		String sql = "SELECT * FROM Login WHERE user_mei LIKE ?;";

		//PostgreSQLへの接続
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement st = con.prepareStatement(sql);) {

			//SQL文の?部分を置き換え
			st.setString(1, user.getName());

			//SQL文の実行
			ResultSet rs = st.executeQuery();

			boolean result = true;

			while (rs.next()) {
				result = false;
			}

			return result;

		} catch (Exception e) {
			System.out.println("DBアクセス時にエラーが発生しました。");
			e.printStackTrace();
			return false;
		}
	}

	//ログイン処理
	public boolean search(String name, String pass) {
		//SQL文の準備
		String sql = "SELECT * FROM Login WHERE user_mei LIKE ? AND password LIKE ?;";

		//PostgreSQLへの接続
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement st = con.prepareStatement(sql);) {

			//SQL文の?部分を置き換え
			st.setString(1, name);
			st.setString(2, pass);

			//SQL文の実行
			ResultSet rs = st.executeQuery();

			boolean result = false;

			while (rs.next()) {
				result = true;
			}

			return result;

		} catch (Exception e) {
			System.out.println("DBアクセス時にエラーが発生しました。");
			e.printStackTrace();
			return false;
		}
	}

	public List<Card> CardAll() {
		/* 1) SQL文の準備 */
		String sql = "SELECT * FROM Card;";

		List<Card> cardList = null;

		/* 2) PostgreSQLへの接続 */
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement st = con.prepareStatement(sql);) {

			/* 3) SQL文の実行 */
			ResultSet rs = st.executeQuery();

			/* 4) 結果をリストに移し替える */
			cardList = makeCardList(rs);

		} catch (Exception e) {
			System.out.println("DBアクセス時にエラーが発生しました。");
			e.printStackTrace();
		}

		return cardList;
	}

	/*
	 * 検索結果をリストで返します。
	 */
	public List<Card> makeCardList(ResultSet rs) throws Exception {
		// 全検索結果を格納するリストを準備
		List<Card> cardList = new ArrayList<Card>();

		while (rs.next()) {
			// 1行分のデータを読込む
			String cardId = rs.getString("card_id");
			String cardMei = rs.getString("card_name");
			String cardType = rs.getString("card_Type");
			int attack = rs.getInt("attack");
			int defense = rs.getInt("defense");
			int heal = rs.getInt("heal");
			String imgPath = rs.getString("img_path");

			// 1行分のデータを格納するインスタンス
			Card card = new Card(cardId,
					cardMei,
					cardType,
					attack,
					defense,
					heal,
					imgPath);

			// リストに1行分のデータを追加する
			cardList.add(card);
		}
		return cardList;
	}
	
	//勝敗をデータベースに記録
	public void achieve(boolean result, User loginUser) {
		//SQL文の準備
		String sql;
		if(result) {
			sql = "UPDATE login SET win = win + 1 WHERE user_mei = ?";
		}else {
			sql = "UPDATE login SET lose = lose + 1 WHERE user_mei = ?";
		}

		//PostgreSQLへの接続
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement st = con.prepareStatement(sql);) {

			//SQL文の?部分を置き換え
			st.setString(1, loginUser.getName());
			
			//SQL文の実行
			st.executeUpdate();	

		} catch (Exception e) {
			System.out.println("DBアクセス時にエラーが発生しました。");
			e.printStackTrace();
		}
	}
	
	public List<User> winLose(User loginUser) {
		/* 1) SQL文の準備 */
		String sql = "SELECT win, lose FROM login WHERE user_mei = ?;";

		List<User> winLoseList = null;

		/* 2) PostgreSQLへの接続 */
		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement st = con.prepareStatement(sql);) {
			
			st.setString(1, loginUser.getName());

			/* 3) SQL文の実行 */
			ResultSet rs = st.executeQuery();

			/* 4) 結果をリストに移し替える */
			winLoseList = makeWinLoseList(rs);

		} catch (Exception e) {
			System.out.println("DBアクセス時にエラーが発生しました。");
			e.printStackTrace();
		}

		return winLoseList;
	}
	
	public List<User> makeWinLoseList(ResultSet rs) throws Exception {
		// 全検索結果を格納するリストを準備
		List<User> winLoseList = new ArrayList<User>();

		while (rs.next()) {
			// 1行分のデータを読込む
			int win = rs.getInt("win");
			int lose = rs.getInt("lose");
		

			// 1行分のデータを格納するインスタンス
			User winLose = new User(win,
					lose);

			// リストに1行分のデータを追加する
			winLoseList.add(winLose);
		}
		return winLoseList;
	}
	
	

}
