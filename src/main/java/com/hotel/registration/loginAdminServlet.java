package com.hotel.registration;

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

@WebServlet("/loginAdmin")
public class loginAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uemail = request.getParameter("username");
		String upass = request.getParameter("password");
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false","root", "1234");
			PreparedStatement pst = con.prepareStatement("SELECT * FROM admin WHERE adminEmail = ? AND password = ?");
			
			pst.setString(1, uemail);
			pst.setString(2, upass);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				dispatcher = request.getRequestDispatcher("indexAdmin.jsp"); //need to add indexAdmin
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

}
