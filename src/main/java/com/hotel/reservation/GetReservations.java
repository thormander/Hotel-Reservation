package com.hotel.reservation;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class GetReservations
 */
@WebServlet("/getReservations") //connection to the .jsp file for intake of data
public class GetReservations extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		viewMyReservations(request, response);
	}
	
	public void viewMyReservations(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String currentUser = (String) session.getAttribute("email");
		RequestDispatcher dispatcher = null;
		
		try 
		{
		
			List<Reservation> myReservations = getMyReservations(request, response, currentUser);
			System.out.println(myReservations);

			request.setAttribute("reservations", myReservations);
			
			dispatcher = request.getRequestDispatcher("viewReservations.jsp");
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public List<Reservation> getMyReservations(HttpServletRequest request, HttpServletResponse response, String currentUser)
	{
		System.out.println(currentUser);
		Connection con = null;
		
		List<Reservation> myReservations = new ArrayList<>();
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=America/Chicago","root", "1234");
			
			
			PreparedStatement pst = con.prepareStatement("SELECT id, startDate, endDate, reservationName, accountType  FROM reservation WHERE reservationName = ?");

			
			pst.setString(1, currentUser);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				Reservation a = new Reservation();
				a.setId(rs.getString("id"));
				a.setStartDate(rs.getString("startDate"));
				a.setEndDate(rs.getString("endDate"));
				myReservations.add(a);
			}
			

		} catch (SQLException | ClassNotFoundException e)
		{  
			e.printStackTrace();
		}
		
		return myReservations;
	}

}
