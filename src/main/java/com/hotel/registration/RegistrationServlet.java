package com.hotel.registration;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@WebServlet("/register") //connection to the .jsp file for intake of data
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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

}
