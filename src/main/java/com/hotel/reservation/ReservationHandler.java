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
import java.util.ArrayList;
import java.util.List;



///////////////////////////////////
@WebServlet("/reservationHandler") //connection to the .jsp file for intake of data
public class ReservationHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String smoking = request.getParameter("smoking");
		String bAmount = request.getParameter("amountBeds");
		String bType = request.getParameter("bedType");
		String quality = request.getParameter("quality");
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false","root", "1234");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			
			//Call the reservation Catalog and room catalog.
			PreparedStatement pst = con.prepareStatement("Select * from rooms where rooms.id NOT IN (SELECT reservation.id FROM reservation WHERE ((? > startDate AND ? < endDate) OR  (? > startDate AND ? < endDate) OR (?< startDate AND ? >endDate))) AND smoking = ? AND bedSize = ? AND amountBeds = ? AND Quality = ?");
			
			
			//from there, we interact with the reservation class itself.
			pst.setString(1,startDate);
			pst.setString(2,startDate);
			pst.setString(3,endDate);
			pst.setString(4,endDate);
			pst.setString(5,startDate);
			pst.setString(6,endDate);
			
			pst.setString(7,smoking);
			pst.setString(8,bType);
			pst.setString(9,bAmount);
			pst.setString(10,quality);
			
		
			
			
			ResultSet rs = pst.executeQuery();
			ArrayList<Room> myList = new ArrayList();
			
			while(rs.next()) {
				Room a = new Room();
				a.setId(rs.getInt("id"));
				a.setamountBeds(rs.getString("amountBeds"));
				a.setbedSize(rs.getString("bedSize"));
				
				a.setSmoking(rs.getString("smoking"));
				a.setQuality(rs.getString("Quality"));
				a.setroomInformation(rs.getString("roomInformation"));
				myList.add(a);
				
			}
			
			request.setAttribute("rs", myList);
			
			dispatcher = request.getRequestDispatcher("availableRoomList.jsp");

			
			
			dispatcher.forward(request, response);
			

			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}