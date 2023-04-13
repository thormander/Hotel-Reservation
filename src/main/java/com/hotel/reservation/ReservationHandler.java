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
import java.util.concurrent.TimeUnit;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.hotel.Account.Account;
import com.hotel.email.EmailController;




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
		String clerkEditButton = request.getParameter("clerkEditReservation");
		String clerkSubmitReservationForm = request.getParameter("clerkSubmitReservation");
		String clerkDeleteButton = request.getParameter("clerkDeleteReservation");



		
		if (reservationStep == "" || reservationStep == null) {
			reservationStep = determineReservationStep(editButton, deleteButton, returnToMyReservations, submitReservationForm,clerkEditButton, clerkSubmitReservationForm, clerkDeleteButton);
		}
		
		System.out.print(reservationStep);
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
		case "checkOutStart": //indexClerk.jsp
			System.out.println("checkout has begun!");
			checkoutStart(request, response);
			break;
		case "checkOutConfirm": //checkOutReservations.jsp
			System.out.println("Confirm checkout has begun!");
			try {
				checkoutBillingConfirm(request, response);  // --> THIS IS A BIG BOI
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case "generateBillingSummary": //index clerk.jsp
			System.out.println("billing summary executed!");
			List<Bill> billingSummary = selectAllBills(request, response);
			request.setAttribute("billSummary", billingSummary);
		    RequestDispatcher dispatcher = request.getRequestDispatcher("generateBillSummary.jsp");
		    dispatcher.forward(request, response);
			break;
		case "clerkViewReservations": //indexClerk.jsp
			clerkViewReservations(request, response);
			break;
		case "clerkEditReservation": //indexClerk.jsp
			clerkViewReservations(request, response, clerkEditButton);
			break;
		case "clerkSubmitReservation": //indexClerk.jsp
			System.out.println("Modify reservation executed!");
			
			String clerkStartDate = request.getParameter("startDate");
			String clerkEndDate = request.getParameter("endDate");
			String guestEmail = request.getParameter("reservationEmail");
			
			if (clerkEndDate == null || clerkStartDate == null || clerkEndDate == "" || clerkStartDate == "") {
				clerkHandleBlankFields(request, response, clerkSubmitReservationForm);
			} else {
				String dateValidation = validateDates(clerkStartDate, clerkEndDate);
				if (dateValidation == "") {
					clerkSubmitReservationUpdates(request, response, clerkSubmitReservationForm, clerkStartDate, clerkEndDate, guestEmail);
				} else {
					clerkInvalidDateRedirect(request, response, dateValidation, clerkSubmitReservationForm);
				}
			}
			break;
		case "clerkDeleteReservation": //loginClerk.jsp
			clerkDeleteReservations(request, response, clerkDeleteButton);
			break;
		default:
			System.out.println("nothing executed in reservationHandler ! :(");
			break;
		}
		
	}
	

	public List<Bill> selectAllBills(HttpServletRequest request, HttpServletResponse response)
	{
		
		Connection con = null;
		
		List<Bill> myBills = new ArrayList<>();
		try 
		{ 
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "12345678");
			PreparedStatement pst = con.prepareStatement("Select * from storage");
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				Bill a = new Bill();
				a.setEmail(rs.getString("guest_email"));
				a.setRoomID(rs.getString("reservationID"));
				a.setCheckInDate(rs.getString("checkInDate"));
				a.setStartDate(rs.getString("startDate"));
				a.setEndDate(rs.getString("endDate"));
				a.setDuration(rs.getString("durationOfStay"));
				a.setCostOfStay(rs.getString("costOfStay"));
				myBills.add(a);
				
			}
		} catch (SQLException | ClassNotFoundException e)
		{  
			e.printStackTrace();
		}
		return myBills;
	}
	
	/*checkoutBillingConfirm:
	 *	This function handles generating a bill and checkout of guest (also adds points on checkout based on each reservation)
	 * */	
	private void checkoutBillingConfirm(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException {
	    
		String actionType = request.getParameter("actionType");
		
		
		//represents the two buttons on the front end
		if ("confirmCheckout".equals(actionType)) { // handles checkout button <------------
			String reservationId = request.getParameter("reservationId");
		    String guestType = request.getParameter("corporateGuest");
		    String guestEmail = request.getParameter("guestCheckoutEmail");
		    
			RequestDispatcher dispatcher = null;
		    
		    if ("corpo".equals(guestType)) { // handle corporate guest
		        System.out.println("Corporate Guest Reservation Checked out!");
				try {
			    	Class.forName("com.mysql.cj.jdbc.Driver");
					//CONNECTION TO DB (change "hotel" to whatever you database name is.)
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "12345678");
					
					//Fetch data for storage------------------------------------
					String fetchQuery = "SELECT r.startDate,r.endDate,r.checkInDate,a.employer FROM reservation r JOIN account a ON(r.reservationName = a.email_id) WHERE r.reservationName = ?";
					PreparedStatement pstStorageFetch = con.prepareStatement(fetchQuery);
					pstStorageFetch.setString(1, guestEmail);
					ResultSet storageFetchQuery = pstStorageFetch.executeQuery();
				    
				    if (storageFetchQuery.next()) {
						// Calculate the duration in days
					    java.sql.Date startDate = storageFetchQuery.getDate("startDate");
					    java.sql.Date endDate = storageFetchQuery.getDate("endDate");
					    long durationInMillis = endDate.getTime() - startDate.getTime();
					    int durationInDays = (int) TimeUnit.DAYS.convert(durationInMillis, TimeUnit.MILLISECONDS);
				    	
				    	//Insert data to storage
						PreparedStatement pstStorage = con.prepareStatement("INSERT INTO storage(reservationID,startDate,endDate,checkInDate,guest_email,employer,durationOfStay) values(?,?,?,?,?,?,?)");
						pstStorage.setString(1,reservationId);
						pstStorage.setDate(2,storageFetchQuery.getDate("startDate"));
						pstStorage.setDate(3,storageFetchQuery.getDate("endDate"));
						pstStorage.setDate(4,storageFetchQuery.getDate("checkInDate"));
						pstStorage.setString(5,guestEmail);
						pstStorage.setString(6,storageFetchQuery.getString("employer"));
						pstStorage.setInt(7,durationInDays);
						pstStorage.execute();
						System.out.println("Data Saved to storage!");
					}
					// ---------------------------------------------------------
					
					PreparedStatement pst = con.prepareStatement("DELETE FROM reservation WHERE reservationID = ?");				
					
					pst.setString(1, reservationId);
					pst.executeUpdate();
					
					//Fetch current points and add 1 point to the guest account---
					PreparedStatement pstPoints = con.prepareStatement("SELECT points FROM account WHERE email_id = ?");
					pstPoints.setString(1, guestEmail);
					ResultSet prs = pstPoints.executeQuery();
					int currentPoints = 0;
					if (prs.next()) {
					    currentPoints = prs.getInt("points");
					}
					int updatedPoints = currentPoints + 1;
					PreparedStatement pstUpdatePoints = con.prepareStatement("UPDATE account SET points = ? WHERE email_id = ?");
					pstUpdatePoints.setInt(1, updatedPoints);
					pstUpdatePoints.setString(2, guestEmail);
					pstUpdatePoints.executeUpdate();
					System.out.println("One point added!");
					//------------------------------------------------------------
					
					//send out email for checkout
					EmailController emailController = new EmailController();
					emailController.sendCheckoutConfirmationEmail(guestEmail);
					
			        //Pass guest email to 'checkoutStart' function to have list pop up again
			        HttpSession session = request.getSession();
			        session.setAttribute("guestEmail", guestEmail);
					checkoutStart(request,response);	
				} catch (Exception e) {
					e.printStackTrace();
				}

		    } else { // handle regular guest
		    	System.out.println("Regular Guest Reservation Checked out!");
				try {
			    	Class.forName("com.mysql.cj.jdbc.Driver");
					//CONNECTION TO DB (change "hotel" to whatever you database name is.)
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "12345678");
					
					
					//Fetch data for storage------------------------------------
					String fetchQuery = "SELECT r.startDate,r.endDate,r.checkInDate,a.employer FROM reservation r JOIN account a ON(r.reservationName = a.email_id) WHERE r.reservationName = ?";
					PreparedStatement pstStorageFetch = con.prepareStatement(fetchQuery);
					pstStorageFetch.setString(1, guestEmail);
					ResultSet storageFetchQuery = pstStorageFetch.executeQuery();
				    
				    if (storageFetchQuery.next()) {
						// Calculate the duration in days
					    java.sql.Date startDate = storageFetchQuery.getDate("startDate");
					    java.sql.Date endDate = storageFetchQuery.getDate("endDate");
					    long durationInMillis = endDate.getTime() - startDate.getTime();
					    int durationInDays = (int) TimeUnit.DAYS.convert(durationInMillis, TimeUnit.MILLISECONDS);
				    	
				    	//Insert data to storage
						PreparedStatement pstStorage = con.prepareStatement("INSERT INTO storage(reservationID,startDate,endDate,checkInDate,guest_email,employer,durationOfStay) values(?,?,?,?,?,?,?)");
						pstStorage.setString(1,reservationId);
						pstStorage.setDate(2,storageFetchQuery.getDate("startDate"));
						pstStorage.setDate(3,storageFetchQuery.getDate("endDate"));
						pstStorage.setDate(4,storageFetchQuery.getDate("checkInDate"));
						pstStorage.setString(5,guestEmail);
						pstStorage.setString(6,storageFetchQuery.getString("employer"));
						pstStorage.setInt(7,durationInDays);
						pstStorage.execute();
						System.out.println("Data Saved to storage!");
					}
					// ---------------------------------------------------------
					
					
					PreparedStatement pst = con.prepareStatement("DELETE FROM reservation WHERE reservationID = ?");				
					pst.setString(1, reservationId);
					pst.executeUpdate();
					
					
					//Fetch current points and add 1 point to the guest account---
					PreparedStatement pstPoints = con.prepareStatement("SELECT points FROM account WHERE email_id = ?");
					pstPoints.setString(1, guestEmail);
					ResultSet prs = pstPoints.executeQuery();
					int currentPoints = 0;
					if (prs.next()) {
					    currentPoints = prs.getInt("points");
					}
					int updatedPoints = currentPoints + 1;
					PreparedStatement pstUpdatePoints = con.prepareStatement("UPDATE account SET points = ? WHERE email_id = ?");
					pstUpdatePoints.setInt(1, updatedPoints);
					pstUpdatePoints.setString(2, guestEmail);
					pstUpdatePoints.executeUpdate();
					System.out.println("One point added!");
					//------------------------------------------------------------
					
					//send out email for checkout
					EmailController emailController = new EmailController();
					emailController.sendCheckoutConfirmationEmail(guestEmail);
					
			        //Pass guest email to 'checkoutStart' function to have list pop up again
			        HttpSession session = request.getSession();
			        session.setAttribute("guestEmail", guestEmail);
					checkoutStart(request,response);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
			
		} else if ("generateBill".equals(actionType)) { // handle generate bill button  <------------
			String reservationId = request.getParameter("reservationId");
		    String guestType = request.getParameter("corporateGuest");
		    String guestEmail = request.getParameter("guestCheckoutEmail");
			
			RequestDispatcher dispatcher = null;
		    
		    if ("corpo".equals(guestType)) { // handle corporate guest
		        System.out.println("Corporate Guest bill generated!");
				try {
			    	Class.forName("com.mysql.cj.jdbc.Driver");
					//CONNECTION TO DB (change "hotel" to whatever you database name is.)
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "12345678");

			        String query = "SELECT r.durationOfStay * (CASE rm.Quality "
					                + "                    WHEN 'Excellent' THEN 150 "
					                + "                    WHEN 'Good' THEN 100 "
					                + "                    WHEN 'Average' THEN 80 "
					                + "                    WHEN 'Poor' THEN 50 "
					                + "                    ELSE 0 "
					                + "                    END) as amountDue "
					                + "FROM reservation r JOIN rooms rm USING(id) WHERE r.reservationID = ?";
			        
					PreparedStatement pst = con.prepareStatement(query);
					pst.setString(1, reservationId);
					
					ResultSet rs = pst.executeQuery();
					double amountDue = 0;
					if (rs.next()) {
					    amountDue = rs.getDouble("amountDue");
					}
					
				    //Get the company name that the guest works for
				    PreparedStatement pst2 = con.prepareStatement("SELECT employer FROM account WHERE email_id = ?");
				    pst2.setString(1, guestEmail);
				    
				    ResultSet rs2 = pst2.executeQuery();
				    String employer = "";
				    if (rs2.next()) {
				    	employer = rs2.getString("employer");
				    }				
					
					request.setAttribute("amountDue", amountDue);
					request.setAttribute("guestType", "corpo"); // Forward the guestType
					request.setAttribute("employer", employer); // Forward company name

					dispatcher = request.getRequestDispatcher("generateBill.jsp");
					dispatcher.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}

		    } else { // handle regular guest
		    	System.out.println("Regular Guest bill generated!");
				try {
			    	Class.forName("com.mysql.cj.jdbc.Driver");
					//CONNECTION TO DB (change "hotel" to whatever you database name is.)
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "12345678");

			        String query = "SELECT r.durationOfStay * (CASE rm.Quality "
					                + "                    WHEN 'Excellent' THEN 150 "
					                + "                    WHEN 'Good' THEN 100 "
					                + "                    WHEN 'Average' THEN 80 "
					                + "                    WHEN 'Poor' THEN 50 "
					                + "                    ELSE 0 "
					                + "                    END) as amountDue "
					                + "FROM reservation r JOIN rooms rm USING(id) WHERE r.reservationID = ?";
			        
					PreparedStatement pst = con.prepareStatement(query);
					pst.setString(1, reservationId);
					
					ResultSet rs = pst.executeQuery();
					double amountDue = 0;
					if (rs.next()) {
					    amountDue = rs.getDouble("amountDue");
					}
					request.setAttribute("amountDue", amountDue);
					request.setAttribute("guestType", "regular"); // Forward the guestType
					
					dispatcher = request.getRequestDispatcher("generateBill.jsp");
					dispatcher.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
			
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
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "12345678");
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
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "12345678");
			
			//Call the reservation Catalog and room catalog.
			PreparedStatement pst = con.prepareStatement("Insert into reservation (id,startDate,endDate,reservationName,accountType,isCheckedIn,durationOfStay) values (?,?,?,?,?,'0',?)");
		
			String startDate = (String) session.getAttribute("startDate");
			String endDate = (String) session.getAttribute("endDate");
			String reservationName = (String) session.getAttribute("email");
			String accountType = (String) session.getAttribute("type");
			
			// Calculate the duration in days
	        LocalDate startLocalDate = LocalDate.parse(startDate);
	        LocalDate endLocalDate = LocalDate.parse(endDate);
	        long durationInDays = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
			
			pst.setString(1, id);
			pst.setString(2, startDate);
			pst.setString(3, endDate);
			pst.setString(4, reservationName);
			pst.setString(5, accountType);
			pst.setLong(6, durationInDays);
			
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
		doGet(request, response);
	}
	
	public void deleteReservations(HttpServletRequest request, HttpServletResponse response, String deleteParams) {
		HttpSession session = request.getSession();
		String currentUser = (String) session.getAttribute("email");
		String accountType = (String) session.getAttribute("type");
		System.out.println("Account type: " + accountType);
		RequestDispatcher dispatcher = null;
		try 
		{
			int reservation_id =  Integer.parseInt(deleteParams);

			Connection con = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=America/Chicago","root", "12345678");
			
			
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
		HttpSession session = request.getSession();
		String accountType = (String) session.getAttribute("type");

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
		String accountType = (String) session.getAttribute("type");
		RequestDispatcher dispatcher = null;
		String[] submitReservationParams = submitReservationForm.split("-");
		
		try 
		{
			
			int reservation_id =  Integer.parseInt(submitReservationParams[1]);
			int room_id = Integer.parseInt(submitReservationParams[0]);

			Connection con = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=America/Chicago","root", "12345678");
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

				if (accountType == "guest") {
					viewMyReservations(request, response);
				} else if (accountType == "clerk") {
					clerkViewReservations(request, response);
				}
			}
			
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void invalidDateRedirect(HttpServletRequest request, HttpServletResponse response, String dateValidation, String submitReservationForm) {
		RequestDispatcher dispatcher = null;
		HttpSession session = request.getSession();
		String accountType = (String) session.getAttribute("type");
		System.out.print("invalid date direct type:" + accountType);

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
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=America/Chicago","root", "12345678");
			
			
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

	public String determineReservationStep(String editButton, String deleteButton, String returnToMyReservations, String submitReservationForm, String clerkEditButton, String clerkSubmitReservationForm, String clerkDeleteButton) {
		String reservationStep = "";
		
		if (editButton != null) {
			reservationStep = "editReservation";
		} else if (deleteButton != null) {
			reservationStep = "deleteReservation";
		} else if (returnToMyReservations != null) {
			reservationStep = "returnToMyReservations";

		} else if (submitReservationForm != null) {
			reservationStep = "submitReservationModify";
		} else if (clerkEditButton != null) {
			reservationStep = "clerkEditReservation";
		} else if (clerkSubmitReservationForm != null) {
			reservationStep = "clerkSubmitReservation";
		} else if (clerkDeleteButton != null) {
			reservationStep = "clerkDeleteReservation";
		}
		
		
		return reservationStep;
	}
	
	//checkout for client  (if testing this, make sure to change to 1 for isCheckedIn)
	public void checkoutStart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
	
		try 
		{
			String email = null;
			
			//For session email check when function called by checkoutConfirm
			if (session.getAttribute("guestName") != null) {
				email = (String) session.getAttribute("guestName");
			} else {
				email = request.getParameter("emailName");
			}
			
			RequestDispatcher dispatcher = null;
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				//CONNECTION TO DB (change "hotel" to whatever you database name is.)
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "12345678");
				//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
				PreparedStatement pst = con.prepareStatement("SELECT * FROM reservation WHERE reservationName = ? AND accountType='guest' AND isCheckedIn='1'");
				
				pst.setString(1, email);
				session.setAttribute("guestName", email);
				
				ResultSet rs = pst.executeQuery();
				List<Reservation> myList = new ArrayList<>();
				
				while(rs.next()) {
					Reservation a = new Reservation();
					a.setRoomId(rs.getString("id"));
					a.setStartDate(rs.getString("startDate"));
					a.setEndDate(rs.getString("endDate"));
					a.setCheckInDate(rs.getString("checkInDate"));
					a.setReservationName(rs.getString("reservationName"));
					a.setId(rs.getString("reservationID")); //needed for tracking to query exact reservation
					myList.add(a);
					
				}
				
				request.setAttribute("guestReservation", myList);
				
				dispatcher = request.getRequestDispatcher("checkOutReservations.jsp");
				dispatcher.forward(request, response);
			
				
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}

	}

	public List<Reservation> getAllReservations(HttpServletRequest request, HttpServletResponse response){
		Connection con = null;
		
		List<Reservation> myReservations = new ArrayList<>();
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=America/Chicago","root", "12345678");
			
			
			PreparedStatement pst = con.prepareStatement("SELECT reservationID, reservation.id AS 'roomID', rooms.roomInformation AS 'roomInformation', startDate, endDate, reservationName FROM reservation RIGHT JOIN rooms ON reservation.id = rooms.id WHERE reservation.id IS NOT NULL;");

			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				if (rs.getString("reservationID") != "" || rs.getString("reservationID") != null) {
				Reservation a = new Reservation();
				a.setId(rs.getString("reservationID"));
				a.setRoomId(rs.getString("roomID"));
				a.setRoomInformation(rs.getString("roomInformation"));
				a.setStartDate(rs.getString("startDate"));
				a.setEndDate(rs.getString("endDate"));
				a.setReservationName(rs.getString("reservationName"));
				myReservations.add(a);
				}
			}
			

		} catch (SQLException | ClassNotFoundException e)
		{  
			e.printStackTrace();
		}
		
		return myReservations;
	}
	
	public void clerkViewReservations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		System.out.println("View Reservations type: " + session.getAttribute("type"));
		RequestDispatcher dispatcher = null;
		
		try 
		{
		
			List<Reservation> myReservations = getAllReservations(request, response);
			request.setAttribute("reservations", myReservations);
			
			dispatcher = request.getRequestDispatcher("clerkViewReservations.jsp");
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void clerkViewReservations(HttpServletRequest request, HttpServletResponse response, String editParams) {
		HttpSession session = request.getSession();
		String currentUser = (String) session.getAttribute("email");
		RequestDispatcher dispatcher = null;
		System.out.println("View Reservations Form type: " + session.getAttribute("type"));
		System.out.println("Edit Params: " + editParams);
		
		try 
		{
			request.setAttribute("reservationId", editParams);
			
			dispatcher = request.getRequestDispatcher("clerkReservationsForm.jsp");
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void clerkSubmitReservationUpdates(HttpServletRequest request, HttpServletResponse response, String submitReservationForm, String startDate, String endDate, String guestEmail) {
		HttpSession session = request.getSession();
		System.out.println("Submit Reservation form: " + submitReservationForm);
	
		String accountType = (String) session.getAttribute("type");
		RequestDispatcher dispatcher = null;
		String[] submitReservationParams = submitReservationForm.split("-");
		String currentUser = submitReservationParams[2];

		
		try 
		{
			
			int reservation_id =  Integer.parseInt(submitReservationParams[1]);
			int room_id = Integer.parseInt(submitReservationParams[0]);

			Connection con = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=America/Chicago","root", "12345678");
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
				
				dispatcher = request.getRequestDispatcher("clerkReservationsForm.jsp");
				dispatcher.forward(request, response);
			} else {
				PreparedStatement updateReservation = con.prepareStatement("UPDATE reservation SET startDate = ? , endDate = ? WHERE reservationId = ?;");

				
				updateReservation.setString(1, startDate);
				updateReservation.setString(2, endDate);
				updateReservation.setInt(3, reservation_id);

				int rowsUpdated = updateReservation.executeUpdate();
				
				clerkViewReservations(request, response);
			}
			
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void clerkInvalidDateRedirect(HttpServletRequest request, HttpServletResponse response, String dateValidation, String submitReservationForm) {
		RequestDispatcher dispatcher = null;
		HttpSession session = request.getSession();
		String accountType = (String) session.getAttribute("type");
		System.out.print("invalid date direct type:" + accountType);

		try {
			request.setAttribute("warning", dateValidation);
			request.setAttribute("reservationId", submitReservationForm);
		
			dispatcher = request.getRequestDispatcher("clerkReservationsForm.jsp");
			dispatcher.forward(request, response);
			
			
		} catch (Exception e) {
			
		}
	}

	public void clerkHandleBlankFields(HttpServletRequest request, HttpServletResponse response, String submitReservationForm) {
		
		RequestDispatcher dispatcher = null;
		String[] submitReservationParams = submitReservationForm.split("-");
		HttpSession session = request.getSession();
		String accountType = (String) session.getAttribute("type");

		try 
		{
			String reservation_id =  submitReservationParams[1];
			String room_id = submitReservationParams[0];
			
			request.setAttribute("warning", "One or more of your fields are blank, please be sure to set both dates.");
			request.setAttribute("reservationId", reservation_id);
			
			dispatcher = request.getRequestDispatcher("clerkReservationsForm.jsp");
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void clerkDeleteReservations(HttpServletRequest request, HttpServletResponse response, String deleteParams) {
		HttpSession session = request.getSession();
		String currentUser = (String) session.getAttribute("email");
		String accountType = (String) session.getAttribute("type");
		System.out.println("Account type: " + accountType);
		RequestDispatcher dispatcher = null;
		try 
		{
			int reservation_id =  Integer.parseInt(deleteParams);

			Connection con = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=America/Chicago","root", "12345678");
			
			
			PreparedStatement reservationDelete = con.prepareStatement("DELETE FROM reservation WHERE reservationID = ?;");

			reservationDelete.setInt(1, reservation_id);
			int rowsDeleted = reservationDelete.executeUpdate();
			
			clerkViewReservations(request, response);
		
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}