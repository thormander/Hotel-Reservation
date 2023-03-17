package com.hotel.Account;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.hotel.reservation.Room;

/**
 * Servlet implementation class AccountHandler
 */
@WebServlet("/accountHandler")
public class AccountHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    //doGet gets called 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String accountType = request.getParameter("accountType");
		switch(accountType)
		
		{
		case "guest":
			System.out.print("here2");
			GuestSignIn(request,response);
			break;
			
		case "clerk":
			ClerkSignIn(request,response);
			break;
		
		case "admin":
			AdminSignIn(request,response);
			
			break;
				
		default:
			System.out.print(":)");
			break;
		}
		
	}

	private void AdminSignIn(HttpServletRequest request, HttpServletResponse response) {

		String uemail = request.getParameter("username");
		String upass = request.getParameter("password");
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false","root", "1234");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			PreparedStatement pst = con.prepareStatement("SELECT * FROM account WHERE email_id = ? AND password = ? AND type = 'admin'");
			
			pst.setString(1, uemail);
			pst.setString(2, upass);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				session.setAttribute("name", rs.getString("user_name"));
				dispatcher = request.getRequestDispatcher("indexAdmin.jsp");
			}
			else
			{
				request.setAttribute("status", "failed");
				dispatcher = request.getRequestDispatcher("loginAdmin.jsp");
			}
			
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	private void ClerkSignIn(HttpServletRequest request, HttpServletResponse response) {
		String uemail = request.getParameter("username");
		String upass = request.getParameter("password");
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false","root", "1234");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			PreparedStatement pst = con.prepareStatement("SELECT * FROM account WHERE email_id = ? AND password = ? AND type = 'clerk'");
			
			pst.setString(1, uemail);
			pst.setString(2, upass);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				session.setAttribute("name", rs.getString("user_name"));
				dispatcher = request.getRequestDispatcher("indexClerk.jsp");
			}
			else
			{
				request.setAttribute("status", "failed");
				dispatcher = request.getRequestDispatcher("loginClerk.jsp");
			}
			
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	public void GuestSignIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		String uemail = request.getParameter("username");
		String upass = request.getParameter("password");
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false","root", "1234");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			PreparedStatement pst = con.prepareStatement("SELECT * FROM account WHERE email_id = ? AND password = ? AND type = 'guest'");
			
			pst.setString(1, uemail);
			pst.setString(2, upass);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				session.setAttribute("name", rs.getString("user_name"));
				dispatcher = request.getRequestDispatcher("index.jsp");
			}
			else
			{
				request.setAttribute("status", "failed");
				dispatcher = request.getRequestDispatcher("login.jsp");
			}
			
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
