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
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String reservationStep = request.getParameter("reservationStep");
		switch(reservationStep)
		
		{
		case "startReservation": //login.jsp
			System.out.println("start reservation executed!");
			StartReservation(request,response);
			break;
			
		case "createReservation": //loginClerk.jsp
			System.out.println("create reservation executed!");
			CreateReservation(request,response);
			break;
			
		default:
			System.out.print("nothing executed in reservationHandler ! :(");
			break;
		}
		
	}
	
	private void StartReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "1234");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			
			//Call the reservation Catalog and room catalog.
			PreparedStatement pst = con.prepareStatement("Select * from rooms where rooms.id NOT IN (SELECT reservation.id FROM reservation WHERE ((? >= startDate AND ? <= endDate) OR  (? >= startDate AND ? <= endDate))) AND smoking = ? AND bedSize = ? AND amountBeds = ? AND Quality = ?");
			
			// Old Statement
			//(? > startDate AND ? < endDate) OR  (? > startDate AND ? < endDate) OR (?< startDate AND ? >endDate)
			
			// Conflicting Dates
			
			// Start date is within another reservation
			// ? >= startDate where ? is new startDate
			// ? <= endDate where ? is new startDate
			
			// End date is within another reservation
			// ? >= startDate where ? is new endDate
			// ? <= endDate where ? is new endDate
			
			//from there, we interact with the reservation class itself.
			pst.setString(1,startDate);
			pst.setString(2,startDate);
			pst.setString(3,endDate);
			pst.setString(4,endDate);
			
			pst.setString(5,smoking);
			pst.setString(6,bType);
			pst.setString(7,bAmount);
			pst.setString(8,quality);
			
		
			
			
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
			
			// Needed for Create Reservation
			session.setAttribute("startDate", startDate);
			session.setAttribute("endDate", endDate);
			
			request.setAttribute("rs", myList);
			
			dispatcher = request.getRequestDispatcher("availableRoomList.jsp");

			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void CreateReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("roomId");
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "1234");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			
			//Call the reservation Catalog and room catalog.
			PreparedStatement pst = con.prepareStatement("Insert into reservation (id,startDate,endDate,reservationName,accountType) values (?,?,?,?,?)");
		
			String startDate = (String) session.getAttribute("startDate");
			String endDate = (String) session.getAttribute("endDate");
			String reservationName = (String) session.getAttribute("email");
			String accountType = (String) session.getAttribute("type");
			
			pst.setString(1, id);
			pst.setString(2, startDate);
			pst.setString(3, endDate);
			pst.setString(4, reservationName);
			pst.setString(5, accountType);
			
			pst.executeUpdate();
			
			Reservation reservation = new Reservation();
			reservation.setId(id);
			reservation.setStartDate(startDate);
			reservation.setEndDate(endDate);
			reservation.setReservationName(reservationName);
			reservation.setAccountType(accountType);
			
			request.setAttribute("ri", reservation);
			
			dispatcher = request.getRequestDispatcher("reservationConfirmation.jsp");
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

}