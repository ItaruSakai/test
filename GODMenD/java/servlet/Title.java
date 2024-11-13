package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DAO;
import model.User;

@WebServlet("/Title")
public class Title extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		dispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		
		
		//データベースと照合し、登録があればログイン処理を実行
		DAO dao = new DAO();
		boolean result = dao.search(name, pass);
		
		
		if(result) {
			User loginUser = new User(name, pass);
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", loginUser);
			request.setAttribute("successMsg", "ログインに成功しました");
		}else {
			request.setAttribute("errorMsg", "入力したユーザー名またはパスワードが違います");
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		dispatcher.forward(request, response);
	}

}
