package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAO;
import model.User;

@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
		dispatcher.forward(request, response);
	} 

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String pass1 = request.getParameter("pass1");

		
		//ユーザー名、パスワードが入力されていれば登録処理を実行
		if (!name.isEmpty() && !pass.isEmpty() && !pass1.isEmpty()) {
			
			//パスワード2回の入力が合致しているか確認
			if (pass.equals(pass1)) {
				
				//Userインスタンス生成
				User user = new User(name, pass);

				//データベースと照合し、ユーザー名の被りが無いか確認
				DAO dao = new DAO();
				if (dao.DuplicateUser(user)) {
					//データベースにユーザー情報を格納
					if(dao.AddUser(user)){
						request.setAttribute("tourokuMsg", "登録が完了しました。");
						RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
						dispatcher.forward(request, response);
					} else {
						request.setAttribute("errorMsg", "登録処理時に問題が発生しました。");
					}
				} else {
					request.setAttribute("errorMsg", "そのユーザーはすでに登録されています。");
				}
			} else {
				request.setAttribute("errorMsg", "入力されたパスワードが一致しません");
			}
		} else {
			request.setAttribute("errorMsg", "ユーザー名、パスワードは必須項目です");
		}
		//フォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/register.jsp");
		dispatcher.forward(request, response);

	}
}
