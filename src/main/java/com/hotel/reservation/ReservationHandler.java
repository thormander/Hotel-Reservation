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
import java.sql.SQLException;




///////////////////////////////////
@WebServlet("/reservationHandler") //connection to the .jsp file for intake of data
public class ReservationHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String reservationStep = request.getParameter("reservationStep");
		String editButton = request.getParameter("editReservation");
		String deleteButton = request.getParameter("deleteReservation");
		String returnToMyReservations = request.getParameter("myReservations");
		String submitReservationForm = request.getParameter("submitReservation");
		
		if (reservationStep == "" || reservationStep == null) {
			reservationStep = determineReservationStep(editButton, deleteButton, returnToMyReservations, submitReservationForm);
		}
		
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
		
		case "editReservation": //loginClerk.jsp
			System.out.println("edit reservation executed!");
			viewMyReservations(request, response, editButton);
			break;
			
		case "deleteReservation": //loginClerk.jsp
			System.out.println("delete reservation executed!");
			deleteReservations(request, response, deleteButton);
			break;
		case "getReservations" : //index.jsp
			System.out.println("getReservation executed!");
			viewMyReservations(request, response);
			break;
			
		case "submitReservationModify": //loginClerk.jsp
			System.out.println("Modify reservation executed!");
			
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			System.out.print("Start Date: ");
			System.out.println(startDate);
			System.out.print("End Date: ");
			System.out.println(endDate);
			
			if (endDate == null || startDate == null || endDate == "" || startDate == "") {
				handleBlankFields(request, response, submitReservationForm);
			} else {
				String dateValidation = validateDates(startDate, endDate);
				if (dateValidation == "") {
					submitReservationUpdates(request, response, submitReservationForm, startDate, endDate);
				} else {
					invalidDateRedirect(request, response, dateValidation, submitReservationForm);
				}
			}
			break;
			
		case "returnToMyReservations": //loginClerk.jsp
			System.out.println("Return to reservations executed!");
			viewMyReservations(request, response);
			break;
			
		default:
			System.out.print("nothing executed in reservationHandler ! :(");
			break;
		}
		
	}
	
	public void StartReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String smoking = request.getParameter("smoking");
		String bAmount = request.getParameter("amountBeds");
		String bType = request.getParameter("bedType");
		String quality = request.getParameter("quality");
		
		String dates = validateDates(startDate,endDate);
		
		
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		if (dates == "" ) {

			try 
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				//CONNECTION TO DB (change "hotel" to whatever you database name is.)
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "1234");
				//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
				
				//Call the reservation Catalog and room catalog.
				PreparedStatement pst = con.prepareStatement("Select * from rooms where rooms.id NOT IN (SELECT reservation.id FROM reservation WHERE ((? >= startDate AND ? <= endDate) OR  (? >= startDate AND ? <= endDate) OR (? <= startDate AND ? >= endDate))) AND smoking = ? AND bedSize = ? AND amountBeds = ? AND Quality = ?");
				
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
				
				// Needed for Create Reservation
				session.setAttribute("startDate", startDate);
				session.setAttribute("endDate", endDate);
				
				request.setAttribute("rs", myList);
				
				dispatcher = request.getRequestDispatcher("availableRoomList.jsp");
	
				dispatcher.forward(request, response);
				
				request.setAttribute("status", "success"); //for unit testing
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		} else {
			request.setAttribute("warning", dates);

            dispatcher = request.getRequestDispatcher("searchRooms.jsp");
            dispatcher.forward(request, response);
		}
	}
	
	public void CreateReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
			
			request.setAttribute("status", "success"); //for unit testing
			
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
	
	public void deleteReservations(HttpServletRequest request, HttpServletResponse response, String deleteParams) {
		HttpSession session = request.getSession();
		String currentUser = (String) session.getAttribute("email");
		RequestDispatcher dispatcher = null;
		try 
		{
			int reservation_id =  Integer.parseInt(deleteParams);

			Connection con = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=America/Chicago","root", "1234");
			
			
			PreparedStatement reservationDelete = con.prepareStatement("DELETE FROM reservation WHERE reservationID = ?;");

			reservationDelete.setInt(1, reservation_id);
			int rowsDeleted = reservationDelete.executeUpdate();
			
			viewMyReservations(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
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

	public void viewMyReservations(HttpServletRequest request, HttpServletResponse response, String editParams) {
		HttpSession session = request.getSession();
		String currentUser = (String) session.getAttribute("email");
		RequestDispatcher dispatcher = null;
		
		try 
		{
			request.setAttribute("reservationId", editParams);
			
			dispatcher = request.getRequestDispatcher("reservationsForm.jsp");
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void handleBlankFields(HttpServletRequest request, HttpServletResponse response, String submitReservationForm) {
		
		RequestDispatcher dispatcher = null;
		String[] submitReservationParams = submitReservationForm.split("-");

		try 
		{
			String reservation_id =  submitReservationParams[1];
			String room_id = submitReservationParams[0];
			
			request.setAttribute("warning", "One or more of your fields are blank, please be sure to set both dates.");
			request.setAttribute("reservationId", reservation_id);
			
			dispatcher = request.getRequestDispatcher("reservationsForm.jsp");
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void submitReservationUpdates(HttpServletRequest request, HttpServletResponse response, String submitReservationForm, String startDate, String endDate) {
		HttpSession session = request.getSession();
		String currentUser = (String) session.getAttribute("email");
		RequestDispatcher dispatcher = null;
		String[] submitReservationParams = submitReservationForm.split("-");
		
		try 
		{
			
			int reservation_id =  Integer.parseInt(submitReservationParams[1]);
			int room_id = Integer.parseInt(submitReservationParams[0]);

			Connection con = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=America/Chicago","root", "1234");
			PreparedStatement currentReservations = con.prepareStatement("SELECT reservationId FROM reservation WHERE id = ? AND reservationName != ? AND ((? >= startDate AND ? <= endDate) OR  (? >= startDate AND ? <= endDate) OR (? <= startDate AND ? >= endDate));");
			currentReservations.setInt(1, room_id);
			currentReservations.setString(2, currentUser);
			currentReservations.setString(3,startDate);
			currentReservations.setString(4,startDate);
			currentReservations.setString(5,endDate);
			currentReservations.setString(6,endDate);
			currentReservations.setString(7,startDate);
			currentReservations.setString(8,endDate);

			ResultSet rs = currentReservations.executeQuery();
			System.out.println(rs);
			if (rs.next()) {
				request.setAttribute("warning", "This room is already reserved for the dates you provided. Please try different dates.");
				request.setAttribute("reservationId", submitReservationForm);
				
				dispatcher = request.getRequestDispatcher("reservationsForm.jsp");
				dispatcher.forward(request, response);
			} else {
				PreparedStatement updateReservation = con.prepareStatement("UPDATE reservation SET startDate = ? , endDate = ? WHERE reservationId = ?;");

				
				updateReservation.setString(1, startDate);
				updateReservation.setString(2, endDate);
				updateReservation.setInt(3, reservation_id);

				int rowsUpdated = updateReservation.executeUpdate();

				
				viewMyReservations(request, response);
			}
			
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void invalidDateRedirect(HttpServletRequest request, HttpServletResponse response, String dateValidation, String submitReservationForm) {
		RequestDispatcher dispatcher = null;

		try {
			request.setAttribute("warning", dateValidation);
			request.setAttribute("reservationId", submitReservationForm);
			
			dispatcher = request.getRequestDispatcher("reservationsForm.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			
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
			
			
			PreparedStatement pst = con.prepareStatement("SELECT reservationID, reservation.id AS 'roomID', rooms.roomInformation AS 'roomInformation', startDate, endDate, reservationName FROM reservation RIGHT JOIN rooms ON reservation.id = rooms.id WHERE reservationName = ?;");

			
			pst.setString(1, currentUser);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				Reservation a = new Reservation();
				a.setId(rs.getString("reservationID"));
				a.setRoomId(rs.getString("roomID"));
				a.setRoomInformation(rs.getString("roomInformation"));
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
	
	public String validateDates(String startDate, String endDate) {
		String warning = "";
		
		int result = startDate.compareTo(endDate);
		
        if (result > 0) {
			warning = "Please correct your dates, the start date you entered was after the end date you entered.";
        } 

		return warning;
	}

	public String determineReservationStep(String editButton, String deleteButton, String returnToMyReservations, String submitReservationForm) {
		String reservationStep = "";
		
		if (editButton != null) {
			reservationStep = "editReservation";
		} else if (deleteButton != null) {
			reservationStep = "deleteReservation";
		} else if (returnToMyReservations != null) {
			reservationStep = "returnToMyReservations";

		} else if (submitReservationForm != null) {
			reservationStep = "submitReservationModify";
		}
		return reservationStep;
	}
	
	
}