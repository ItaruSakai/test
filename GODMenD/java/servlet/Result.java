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

@WebServlet("/Result")
public class Result extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//ユーザー情報を保持したままタイトル画面へ
		request.setCharacterEncoding("UTF-8");
		request.setAttribute("successMsg", "ログイン中です");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Title");
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean result = (boolean) request.getAttribute("result");	
		DAO dao = new DAO();
		HttpSession session = request.getSession();
		User loginUser = (User)session.getAttribute("loginUser");
		
		//ユーザー情報のデータベース上に勝敗を記録
		if (result) {
			request.setAttribute("resultMsg", "勝利");
			dao.achieve(result, loginUser);
		} else {
			request.setAttribute("resultMsg", "敗北");
			dao.achieve(result, loginUser);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/jsp/result.jsp");
		dispatcher.forward(request, response);
	}
}
