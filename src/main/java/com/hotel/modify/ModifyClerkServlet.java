package com.hotel.modify;

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

@WebServlet("/modifyClerk") //connection to the .jsp file for intake of data
public class ModifyClerkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//REPLACING EMAIL WITH FIRST AND LAST NAME 
		
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

}

