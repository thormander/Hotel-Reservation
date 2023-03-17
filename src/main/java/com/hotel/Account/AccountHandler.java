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


/**
 * Servlet implementation class AccountHandler
 */
@WebServlet("/accountHandler")
public class AccountHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    //doGet gets called 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String accountType = request.getParameter("accountType");
		switch(accountType)
		
		{
		case "guest": //login.jsp
			System.out.println("login guest executed!");
			GuestSignIn(request,response);
			break;
			
		case "clerk": //loginClerk.jsp
			System.out.println("login clerk executed!");
			ClerkSignIn(request,response);
			break;
		
		case "admin": //loginAdmin.jsp
			System.out.println("login admin executed!");
			AdminSignIn(request,response);
			break;
		
		case "modifyGuest": //modifyGuest.jsp
			System.out.println("modify guest executed!");
			modifyGuest(request,response);
			break;
		
		case "modifyClerk": //modifyClerk.jsp
			System.out.println("modify clerk executed!");
			modifyClerk(request,response);
			break;
		
		case "registerGuest": //registration.jsp
			System.out.println("register guest executed!");
			registerGuest(request,response);
			break;
		
		case "registerClerk": //indexAdmin.jsp
			System.out.println("register clerk executed!");
			registerClerk(request,response);
			break;	
			
		default:
			System.out.print("nothing executed in accountHandler ! :(");
			break;
		}
		
	}

	private void registerClerk(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("name");
		String uemail = request.getParameter("email");
		String upass = request.getParameter("pass");
		
		RequestDispatcher dispatcher = null;
		Connection con = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false","root", "1234");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			PreparedStatement pst = con.prepareStatement("INSERT INTO account(user_name,email_id,password,type) values(?,?,?,'clerk')");
			pst.setString(1, username);
			pst.setString(2, uemail);
			pst.setString(3, upass);
			
			int rowCount = pst.executeUpdate();
			
			dispatcher = request.getRequestDispatcher("indexAdmin.jsp");
			if (rowCount > 0)
			{
				request.setAttribute("status", "success");
			}
			else
			{
				request.setAttribute("status", "failed");
			}
			
			dispatcher.forward(request, response);
			
		}
		catch (java.sql.SQLIntegrityConstraintViolationException e)
		{
		    request.setAttribute("errorMessage", "Email already exists");
		    request.setAttribute("showError", "true");
		    request.getRequestDispatcher("/indexAdmin.jsp").forward(request, response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally
		{
			try
			{
				con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	private void registerGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("name");
		String uemail = request.getParameter("email");
		String upass = request.getParameter("pass");
		
		if (uemail == "") 
		{
			
		}
		
		RequestDispatcher dispatcher = null;
		Connection con = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false","root", "1234");
			//SQL query to database here
			PreparedStatement pst = con.prepareStatement("INSERT INTO account(user_name,email_id,password,type) values(?,?,?,'guest')");
			pst.setString(1, username);
			pst.setString(2, uemail);
			pst.setString(3, upass);
			
			int rowCount = pst.executeUpdate();
			
			dispatcher = request.getRequestDispatcher("registration.jsp");
			if (rowCount > 0)
			{
				request.setAttribute("status", "success");
			}
			else
			{
				request.setAttribute("status", "failed");
			}
			
			dispatcher.forward(request, response);
			
		}
		catch (java.sql.SQLIntegrityConstraintViolationException e)
		{
		    request.setAttribute("errorMessage", "Email already exists");
		    request.setAttribute("showError", "true");
		    request.getRequestDispatcher("/registration.jsp").forward(request, response);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	private void modifyClerk(HttpServletRequest request, HttpServletResponse response) {
		
		String uemail = request.getParameter("email");
		String upass = request.getParameter("pass");
		String uname = request.getParameter("name");
		String upassOld = request.getParameter("passOld");
		
		RequestDispatcher dispatcher = null;
		Connection con = null;
		
		//"`"
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false","root", "1234");
			//SQL query to database here
			
			PreparedStatement pst = con.prepareStatement("UPDATE `hotel`.`account` SET `password` = ? WHERE (`email_id` = ? AND `user_name` = ? AND `password` = ? AND `type` = 'clerk') ");
			
			pst.setString(1, upass);
			pst.setString(2, uemail);
			pst.setString(3, uname);
			pst.setString(4, upassOld);
			
			pst.executeUpdate();
			
			dispatcher = request.getRequestDispatcher("modifyClerk.jsp");
			
			dispatcher.forward(request, response);
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally
		{
			try
			{
				con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
	}

	private void modifyGuest(HttpServletRequest request, HttpServletResponse response) {
		
		String uemail = request.getParameter("email");
		String upass = request.getParameter("pass");
		String uname = request.getParameter("name");
		String upassOld = request.getParameter("passOld");
		
		RequestDispatcher dispatcher = null;
		Connection con = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false","root", "1234");
			//SQL query to database here
			
			PreparedStatement pst = con.prepareStatement("UPDATE `hotel`.`account` SET `password` = ? WHERE (`email_id` = ? AND `user_name` = ? AND `password` = ? AND `type` = 'guest')");
			
			pst.setString(1, upass);
			pst.setString(2, uemail);
			pst.setString(3, uname);
			pst.setString(4, upassOld);
			
			pst.executeUpdate();
			
			dispatcher = request.getRequestDispatcher("modifyGuest.jsp");
			
			dispatcher.forward(request, response);
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally
		{
			try
			{
				con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
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


/* this is the code that was on logout servlet(currently unused)
 * public class logoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public logoutServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
 * */
