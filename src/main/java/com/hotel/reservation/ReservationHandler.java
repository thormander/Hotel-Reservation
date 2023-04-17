package com.hotel.reservation;



import jakarta.servlet.RequestDispatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;

import com.hotel.Account.Account;
import com.hotel.email.EmailController;




///////////////////////////////////
@WebServlet("/reservationHandler") //connection to the .jsp file for intake of data
public class ReservationHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String reservationAction = request.getParameter("reservationAction");
		String editButton = request.getParameter("editReservation");
		String deleteButton = request.getParameter("deleteReservation");
		String returnToMyReservations = request.getParameter("myReservations");
		String submitReservationForm = request.getParameter("submitReservation");
		String clerkEditButton = request.getParameter("clerkEditReservation");
		String clerkSubmitReservationForm = request.getParameter("clerkSubmitReservation");
		String clerkDeleteButton = request.getParameter("clerkDeleteReservation");



		
		if (reservationAction == "" || reservationAction == null) {
			reservationAction = determinereservationAction(editButton, deleteButton, returnToMyReservations, submitReservationForm,clerkEditButton, clerkSubmitReservationForm, clerkDeleteButton);
		}
		
		System.out.println(reservationAction);
		switch(reservationAction)
		
		{
		case "startReservation": 
			System.out.println("start reservation executed!");
			StartReservation(request,response);
			break;
			
		case "createReservation": 
			System.out.println("create reservation executed!");
			CreateReservation(request,response);
			break;
		
		case "editReservation": 
			System.out.println("edit reservation executed!");
			viewMyReservations(request, response, editButton);
			break;
			
		case "deleteReservation": 
			System.out.println("delete reservation executed!");
			deleteReservations(request, response, deleteButton);
			break;
			
		case "getReservations" : 
			System.out.println("getReservation executed!");
			viewMyReservations(request, response);
			break;
			
		case "submitReservationModify": 
			System.out.println("Modify reservation executed!");
			
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			
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
			
		case "returnToMyReservations": 
			System.out.println("Return to reservations executed!");
			viewMyReservations(request, response);
			break;
		case "checkInStart" : //viewReservationsClerk.jsp
			System.out.println("checkin has begun!");
			checkinStart(request, response);
			break;
		case "checkInConfirm" : //checkInReservations.jsp
			System.out.println("checkin confirmation has begun");
			checkinConfirm(request, response);
			break;
		case "checkOutStart": //viewReservationsClerk.jsp
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
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
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
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
					
					//Fetch data for storage------------------------------------
					//Calculate cost
			        String costQuery = "SELECT r.durationOfStay * (CASE rm.Quality "
			                + "                    WHEN 'Excellent' THEN 150 "
			                + "                    WHEN 'Good' THEN 100 "
			                + "                    WHEN 'Average' THEN 80 "
			                + "                    WHEN 'Poor' THEN 50 "
			                + "                    ELSE 0 "
			                + "                    END) as amountDue "
			                + "FROM reservation r JOIN rooms rm USING(id) WHERE r.reservationID = ?";
					PreparedStatement pst = con.prepareStatement(costQuery);
					pst.setString(1, reservationId);
					ResultSet rs = pst.executeQuery();
					double costOfStay = 0;
					if (rs.next()) {
						costOfStay = rs.getDouble("amountDue");
					}
					//Check for late fees due
					PreparedStatement pstLateStorage = con.prepareStatement("SELECT * FROM reservation WHERE reservationID=? AND checkInDate > startDate;");
					pstLateStorage.setString(1, reservationId);
					ResultSet rsLateStorage = pstLateStorage.executeQuery();
					if (rsLateStorage.next()) {
						costOfStay += 50; //add 50 dollar late fee
					}
					String fetchQuery = "SELECT r.startDate,r.endDate,r.checkInDate,a.employer FROM reservation r JOIN account a ON(r.reservationName = a.email_id) WHERE r.reservationName = ? AND r.reservationID = ?";
					PreparedStatement pstStorageFetch = con.prepareStatement(fetchQuery);
					pstStorageFetch.setString(1, guestEmail);
					pstStorageFetch.setString(2, reservationId);
					ResultSet storageFetchQuery = pstStorageFetch.executeQuery();
				    
				    if (storageFetchQuery.next()) {
						// Calculate the duration in days
					    java.sql.Date startDate = storageFetchQuery.getDate("startDate");
					    java.sql.Date endDate = storageFetchQuery.getDate("endDate");
					    long durationInMillis = endDate.getTime() - startDate.getTime();
					    int durationInDays = (int) TimeUnit.DAYS.convert(durationInMillis, TimeUnit.MILLISECONDS);
				    	
				    	//Insert data to storage
					    PreparedStatement pstStorage = con.prepareStatement("INSERT INTO storage(reservationID,startDate,endDate,checkInDate,guest_email,employer,durationOfStay,costOfStay) values(?,?,?,?,?,?,?,?)");
						pstStorage.setString(1,reservationId);
						pstStorage.setDate(2,storageFetchQuery.getDate("startDate"));
						pstStorage.setDate(3,storageFetchQuery.getDate("endDate"));
						pstStorage.setDate(4,storageFetchQuery.getDate("checkInDate"));
						pstStorage.setString(5,guestEmail);
						pstStorage.setString(6,storageFetchQuery.getString("employer"));
						pstStorage.setInt(7,durationInDays);
						pstStorage.setDouble(8,costOfStay);
						pstStorage.execute();
						System.out.println("Data Saved to storage!");
					}
					// ---------------------------------------------------------
					
				    PreparedStatement pst1 = con.prepareStatement("DELETE FROM reservation WHERE reservationID = ?");				
					
				    pst1.setString(1, reservationId);
					pst1.executeUpdate();
					
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
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
					
					
					//Fetch data for storage------------------------------------
					//Calculate cost
			        String costQuery = "SELECT r.durationOfStay * (CASE rm.Quality "
			                + "                    WHEN 'Excellent' THEN 150 "
			                + "                    WHEN 'Good' THEN 100 "
			                + "                    WHEN 'Average' THEN 80 "
			                + "                    WHEN 'Poor' THEN 50 "
			                + "                    ELSE 0 "
			                + "                    END) as amountDue "
			                + "FROM reservation r JOIN rooms rm USING(id) WHERE r.reservationID = ?";
					PreparedStatement pst = con.prepareStatement(costQuery);
					pst.setString(1, reservationId);
					ResultSet rs = pst.executeQuery();
					double costOfStay = 0;
					if (rs.next()) {
						costOfStay = rs.getDouble("amountDue");
					}
					//Check for late fees due
					PreparedStatement pstLateStorage = con.prepareStatement("SELECT * FROM reservation WHERE reservationID=? AND checkInDate > startDate;");
					pstLateStorage.setString(1, reservationId);
					ResultSet rsLateStorage = pstLateStorage.executeQuery();
					if (rsLateStorage.next()) {
						costOfStay += 50; //add 50 dollar late fee
					}
					
					
					String fetchQuery = "SELECT r.startDate,r.endDate,r.checkInDate,a.employer FROM reservation r JOIN account a ON(r.reservationName = a.email_id) WHERE r.reservationName = ? AND r.reservationID = ?";
					PreparedStatement pstStorageFetch = con.prepareStatement(fetchQuery);
					pstStorageFetch.setString(1, guestEmail);
					pstStorageFetch.setString(2, reservationId);
					ResultSet storageFetchQuery = pstStorageFetch.executeQuery();
				    
				    if (storageFetchQuery.next()) {
						// Calculate the duration in days
					    java.sql.Date startDate = storageFetchQuery.getDate("startDate");
					    java.sql.Date endDate = storageFetchQuery.getDate("endDate");
					    long durationInMillis = endDate.getTime() - startDate.getTime();
					    int durationInDays = (int) TimeUnit.DAYS.convert(durationInMillis, TimeUnit.MILLISECONDS);
				    	
				    	//Insert data to storage
					    PreparedStatement pstStorage = con.prepareStatement("INSERT INTO storage(reservationID,startDate,endDate,checkInDate,guest_email,employer,durationOfStay,costOfStay) values(?,?,?,?,?,?,?,?)");						pstStorage.setString(1,reservationId);
						pstStorage.setDate(2,storageFetchQuery.getDate("startDate"));
						pstStorage.setDate(3,storageFetchQuery.getDate("endDate"));
						pstStorage.setDate(4,storageFetchQuery.getDate("checkInDate"));
						pstStorage.setString(5,guestEmail);
						pstStorage.setString(6,storageFetchQuery.getString("employer"));
						pstStorage.setInt(7,durationInDays);
						pstStorage.setDouble(8,costOfStay);
						pstStorage.execute();
						System.out.println("Data Saved to storage!");
					}
					// ---------------------------------------------------------
					
					
				    PreparedStatement pst1 = con.prepareStatement("DELETE FROM reservation WHERE reservationID = ?");				
					pst1.setString(1, reservationId);
					pst1.executeUpdate();
					
					
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
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");

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
					
					//Check for late fees due
					PreparedStatement pstLate = con.prepareStatement("SELECT * FROM reservation WHERE reservationID=? AND checkInDate > startDate;");
					pstLate.setString(1, reservationId);
					ResultSet rsLate = pstLate.executeQuery();
					if (rsLate.next()) {
						amountDue += 50; //add 50 dollar late fee
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
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");

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
					
					//Check for late fees due
					PreparedStatement pstLate = con.prepareStatement("SELECT * FROM reservation WHERE reservationID=? AND checkInDate > startDate;");
					pstLate.setString(1, reservationId);
					ResultSet rsLate = pstLate.executeQuery();
					if (rsLate.next()) {
						amountDue += 50; //add 50 dollar late fee
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
		
		boolean testMode = "true".equals(request.getParameter("testMode")); //check if request from junit
		
		if (dates == "" ) {

			try 
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				//CONNECTION TO DB (change "hotel" to whatever you database name is.)
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
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
				
				// Write success response for unit test
		        if (testMode) {
			        PrintWriter out = response.getWriter();
			        response.setContentType("text/plain");
			        response.setCharacterEncoding("UTF-8");
			        out.write("success");
			        out.flush();
			        out.close();
		        }
				
				if (testMode == false) {
					dispatcher.forward(request, response);
				}
				
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
		
		boolean testMode = "true".equals(request.getParameter("testMode")); //check if request from junit
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			
			//Call the reservation Catalog and room catalog.
			PreparedStatement pst = con.prepareStatement("Insert into reservation (id,startDate,endDate,reservationName,accountType,durationOfStay,checkInDate,isCheckedIn) values (?,?,?,?,?,?,?,0)");
		
			//Because we are retrieving from session, we need to create separate 
			//variables to retrieve from a request parameter for the junit test
			String startDate;
			String endDate;
			String reservationName;
			String accountType;
			if (testMode) {
			    startDate = request.getParameter("startDate");
			    endDate = request.getParameter("endDate");
			    reservationName = request.getParameter("email");
			    accountType = request.getParameter("type");
			} else {
				startDate = (String) session.getAttribute("startDate");
				endDate = (String) session.getAttribute("endDate");
				reservationName = (String) session.getAttribute("email");
				accountType = (String) session.getAttribute("type");	
			}
			boolean clerkLoggedIn = false;
			
			if (accountType.compareTo("clerk") == 0)
			{
				clerkLoggedIn = true;
				accountType = (String) session.getAttribute("guestType");
				reservationName = (String) session.getAttribute("guestEmailName");
			}
			
			// Calculate the duration in days
	        LocalDate startLocalDate = LocalDate.parse(startDate);
	        LocalDate endLocalDate = LocalDate.parse(endDate);
	        long durationInDays = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
	        
	        // Guest is initially not checked in
	        boolean isCheckedIn = false; 
	        String checkInDate = "Yet to be Checked-In";
			
			pst.setString(1, id);
			pst.setString(2, startDate);
			pst.setString(3, endDate);
			pst.setString(4, reservationName);
			pst.setString(5, accountType);
			pst.setLong(6, durationInDays);
			pst.setString(7,  checkInDate);
			
			pst.executeUpdate();
			
			// Write success response for unit test
	        if (testMode) {
		        PrintWriter out = response.getWriter();
		        response.setContentType("text/plain");
		        response.setCharacterEncoding("UTF-8");
		        out.write("success");
		        out.flush();
		        out.close();
	        }
			
			Reservation reservation = new Reservation();
			reservation.setId(id);
			reservation.setStartDate(startDate);
			reservation.setEndDate(endDate);
			reservation.setReservationName(reservationName);
			reservation.setAccountType(accountType);
			reservation.setIsCheckedIn(isCheckedIn);
			reservation.setCheckInDate(checkInDate);
			
			request.setAttribute("ri", reservation);
			
			dispatcher = request.getRequestDispatcher("reservationConfirmation.jsp");
			if (clerkLoggedIn)
			{
				dispatcher = request.getRequestDispatcher("reservationConfirmationClerk.jsp");
			}
			
			if (testMode == false) {
				dispatcher.forward(request, response);			
			}
			
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
		RequestDispatcher dispatcher = null;
		
		boolean testMode = "true".equals(request.getParameter("testMode")); //check if request from junit
		
		//Because we are retrieving from session, we need to create separate 
		//variables to retrieve from a request parameter for the junit test
		String currentUser;
		String accountType;
		if (testMode) {
		    currentUser = request.getParameter("email");
		    accountType = request.getParameter("type");

		} else {
			currentUser = (String) session.getAttribute("email");
			accountType = (String) session.getAttribute("type");
		}
		
		if (accountType.compareTo("clerk") == 0)
		{
			currentUser = (String) session.getAttribute("guestEmailName");
		}
		
		try 
		{
			int reservation_id;
			if (testMode) {
				reservation_id = Integer.parseInt(request.getParameter("deleteParams"));
			} else {
				reservation_id =  Integer.parseInt(deleteParams);			
			}

			Connection con = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			
			PreparedStatement  billingInfoQueury = con.prepareStatement("SELECT ccNum, ccExp, ccAddress FROM hotel.account WHERE email_id = ?");
			
			/* Obtain billing information from specific guest */
			String billingInfoQuery = "SELECT ccNum, ccExp, ccAddress FROM hotel.account WHERE email_id = ?";
			
			PreparedStatement biQuery = con.prepareStatement(billingInfoQuery);
			biQuery.setString(1, currentUser);
			
			String ccNum = "";
			String ccExp = "";
			String ccAddress = "";
			
			ResultSet rs = biQuery.executeQuery();
			if (rs.next())
			{	
				ccNum = rs.getString("ccNum");
				ccExp = rs.getString("ccExp");
				ccAddress = rs.getString("ccAddress");
			}
			
			/* Getting cancellation date */
			LocalDate localDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String deleteDate = localDate.format(formatter);
		    
			/* Getting access to year, month, day for delete date */
			String[] deleteDateData = deleteDate.split("-");
			
			/* Getting start date information */
			String startDate = "";
			boolean isCheckedIn = false;
			
			PreparedStatement startDateQuery = con.prepareStatement("SELECT startDate, isCheckedIn FROM reservation WHERE reservationID=?");
			startDateQuery.setInt(1, reservation_id);
			rs = startDateQuery.executeQuery();
			if (rs.next())
			{	
				startDate = rs.getString("startDate");
				isCheckedIn = (rs.getInt("isCheckedIn") == 1) ? true : false;
			}
			
			/* Getting access to year, month, day for start date */
			String[] startDateData = startDate.split("-");
					
			/* Year is first index, month is second index, day is third index */
			int year = 0;
			int month = 1;
			int day = 2;
			
			boolean issueCancellationFee = false;
			
			/* Check if cancellation date falls within 2 days of reservation start date */
			if (deleteDateData[year].compareTo(startDateData[year]) == 0 && 
				deleteDateData[month].compareTo(startDateData[month]) == 0 &&
				Integer.valueOf(startDateData[day]) - Integer.valueOf(deleteDateData[day]) <= 2)
			{
				issueCancellationFee = true;
			}
			
			/* Missing billing information from guest */
			if (ccNum.compareTo("") == 0 || ccExp.compareTo("") == 0 || ccAddress.compareTo("") == 0)
			{
				dispatcher = request.getRequestDispatcher("accountbilling.jsp");
				dispatcher.forward(request, response);
			}
			/* Not missing billing information, continue delete */
			else 
			{
				if (isCheckedIn == false)
				{
					if (issueCancellationFee)
					{
						String costQuery = "SELECT 0.80 * (CASE rm.Quality "
					                + "                    WHEN 'Excellent' THEN 150 "
					                + "                    WHEN 'Good' THEN 100 "
					                + "                    WHEN 'Average' THEN 80 "
					                + "                    WHEN 'Poor' THEN 50 "
					                + "                    ELSE 0 "
					                + "                    END) as amountDue "
					                + "FROM reservation r JOIN rooms rm USING(id) WHERE r.reservationID = ?";
						PreparedStatement cancellationFee = con.prepareStatement(costQuery);
						cancellationFee.setInt(1, reservation_id);
						
						rs = cancellationFee.executeQuery();
						if (rs.next())
						{
							session.setAttribute("cancellationFee", rs.getString("amountDue"));
						}
						
						dispatcher = request.getRequestDispatcher("cancellationFee.jsp");
						dispatcher.forward(request, response);
					}
				
					PreparedStatement reservationDelete = con.prepareStatement("DELETE FROM reservation WHERE reservationID = ?;");
					reservationDelete.setInt(1, reservation_id);
					int rowsDeleted = reservationDelete.executeUpdate();
				}
				
				// Write success response for unit test
		        if (testMode) {
			        PrintWriter out = response.getWriter();
			        response.setContentType("text/plain");
			        response.setCharacterEncoding("UTF-8");
			        out.write("success");
			        out.flush();
			        out.close();
		        }
				if (testMode == false && issueCancellationFee == false) {
					viewMyReservations(request, response);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void viewMyReservations(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		
		// Checking whether the Clerk or Guest is viewing reservations
		String currentUser = (String) session.getAttribute("email");
		String accountType = (String) session.getAttribute("type");
		if (accountType.compareTo("clerk") == 0)
		{
			currentUser = (String) session.getAttribute("guestEmailName");
		}
		
		RequestDispatcher dispatcher = null;
		
		try 
		{
		
			List<Reservation> myReservations = getMyReservations(request, response, currentUser);

			request.setAttribute("reservations", myReservations);
			
			if (accountType.compareTo("clerk") == 0)
			{
				dispatcher = request.getRequestDispatcher("viewReservationsClerk.jsp");
			}
			else
			{
				dispatcher = request.getRequestDispatcher("viewReservations.jsp");
			}
			
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
		
		// Checking whether the Clerk or Guest is viewing reservations
		String currentUser = (String) session.getAttribute("email");
		String accountType = (String) session.getAttribute("type");
		if (accountType.compareTo("clerk") == 0)
		{
			currentUser = (String) session.getAttribute("guestEmailName");
		}
		
		RequestDispatcher dispatcher = null;
		String[] submitReservationParams = submitReservationForm.split("-");
		
		try 
		{
			
			int reservation_id =  Integer.parseInt(submitReservationParams[1]);
			int room_id = Integer.parseInt(submitReservationParams[0]);

			Connection con = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
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

				if (accountType.compareTo("guest") == 0) {
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
	
	public Reservation getMyReservation(HttpServletRequest request, HttpServletResponse response, int reservationID) {
		Connection con = null;
		
		Reservation myReservation = new Reservation();
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			
			
			PreparedStatement pst = con.prepareStatement("SELECT * FROM reservation WHERE reservationID=?;");

			
			pst.setInt(1, reservationID);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				myReservation.setId(rs.getString("reservationID"));
				myReservation.setRoomId(rs.getString("id"));
				myReservation.setStartDate(rs.getString("startDate"));
				myReservation.setEndDate(rs.getString("endDate"));
				myReservation.setCheckInDate(rs.getString("checkInDate"));
				myReservation.setIsCheckedIn(rs.getBoolean("isCheckedIn"));
				myReservation.setReservationName(rs.getString("reservationName"));
			}
			

		} catch (SQLException | ClassNotFoundException e)
		{  
			e.printStackTrace();
		}
		
		return myReservation;
		
	}
	
	public List<Reservation> getMyReservations(HttpServletRequest request, HttpServletResponse response, String currentUser)
	{
		Connection con = null;
		
		List<Reservation> myReservations = new ArrayList<>();
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			
			
			PreparedStatement pst = con.prepareStatement("SELECT reservationID, reservation.id AS 'roomID', rooms.roomInformation AS 'roomInformation', startDate, endDate, reservationName, checkInDate FROM reservation RIGHT JOIN rooms ON reservation.id = rooms.id WHERE reservationName = ?;");

			
			pst.setString(1, currentUser);
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()) {
				Reservation a = new Reservation();
				a.setId(rs.getString("reservationID"));
				a.setRoomId(rs.getString("roomID"));
				a.setRoomInformation(rs.getString("roomInformation"));
				a.setStartDate(rs.getString("startDate"));
				a.setEndDate(rs.getString("endDate"));
				a.setCheckInDate(rs.getString("checkInDate"));
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

	public String determinereservationAction(String editButton, String deleteButton, String returnToMyReservations, String submitReservationForm, String clerkEditButton, String clerkSubmitReservationForm, String clerkDeleteButton) {
		String reservationAction = "";
		
		if (editButton != null) {
			reservationAction = "editReservation";
		} else if (deleteButton != null) {
			reservationAction = "deleteReservation";
		} else if (returnToMyReservations != null) {
			reservationAction = "returnToMyReservations";

		} else if (submitReservationForm != null) {
			reservationAction = "submitReservationModify";
		} else if (clerkEditButton != null) {
			reservationAction = "clerkEditReservation";
		} else if (clerkSubmitReservationForm != null) {
			reservationAction = "clerkSubmitReservation";
		} else if (clerkDeleteButton != null) {
			reservationAction = "clerkDeleteReservation";
		}
		
		
		return reservationAction;
	}
	
	//checkin for client
	public void checkinStart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		int reservationID = Integer.parseInt(request.getParameter("reservationID"));
		
		// Checking whether the Clerk or Guest is viewing reservations
		String currentUser = (String) session.getAttribute("email");
		String accountType = (String) session.getAttribute("type");
		if (accountType.compareTo("clerk") == 0)
		{
			currentUser = (String) session.getAttribute("guestEmailName");
		}
		
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Reservation myReservation = getMyReservation(request, response, reservationID);
			
			// To work in checkInReservation.jsp
			request.setAttribute("myreservation", myReservation);
			
			// To work in checkinConfirm
			session.setAttribute("myreservation", myReservation);
			
			dispatcher = request.getRequestDispatcher("checkInReservations.jsp");

			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void checkinConfirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String checkInDate = request.getParameter("checkInDate");
		
		HttpSession session = request.getSession();
		Connection con = null;
		
		// Checking whether the Clerk or Guest is viewing reservations
		//String currentUser = (String) session.getAttribute("email");
		//String accountType = (String) session.getAttribute("type");
		//if (accountType.compareTo("clerk") == 0)
		//{
		//	  currentUser = (String) session.getAttribute("guestEmailName");
		//}
		
		Reservation myReservation = (Reservation) session.getAttribute("myreservation");
		
		String reservationID = myReservation.getId();
		String startDate = myReservation.getStartDate();
		String endDate = myReservation.getEndDate();
		boolean isCheckedIn = myReservation.getIsCheckedIn();
		
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			
			PreparedStatement updateCheckIn = con.prepareStatement("UPDATE reservation SET checkInDate=?, isCheckedIn=? WHERE reservationID=?");
			
			updateCheckIn.setString(1, checkInDate);
			updateCheckIn.setInt(2, 1);
			updateCheckIn.setString(3, reservationID);
			
			// Check if checkInDate is within startDate and endDate as well as preventing overwrites if already checked-in
			if (checkInDate.compareTo(startDate) >= 0 && checkInDate.compareTo(endDate) <= 0 && isCheckedIn == false)
			{
				int updatedRows = updateCheckIn.executeUpdate();
				dispatcher = request.getRequestDispatcher("searchGuest.jsp");
			}
			else 
			{
				dispatcher = request.getRequestDispatcher("checkInReservations.jsp");
			}

			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
				//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
				PreparedStatement pst = con.prepareStatement("SELECT * FROM reservation WHERE reservationName = ? AND accountType='guest' AND isCheckedIn=1");
				
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
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			
			
			PreparedStatement pst = con.prepareStatement("SELECT reservationID, reservation.id AS 'roomID', rooms.roomInformation AS 'roomInformation', startDate, endDate, reservationName, checkInDate FROM reservation RIGHT JOIN rooms ON reservation.id = rooms.id WHERE reservation.id IS NOT NULL;");

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
				a.setCheckInDate(rs.getString("checkInDate"));
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
			
			dispatcher = request.getRequestDispatcher("viewAllReservations.jsp");
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void clerkViewReservations(HttpServletRequest request, HttpServletResponse response, String editParams) {
		HttpSession session = request.getSession();
		String currentUser = (String) session.getAttribute("email");
		String accountType = (String) session.getAttribute("type");
		
		RequestDispatcher dispatcher = null;
		System.out.println("View Reservations Form type: " + session.getAttribute("type"));
		System.out.println("Edit Params: " + editParams);
		
		try 
		{
			request.setAttribute("reservationId", editParams);
			session.setAttribute("startDate", request.getAttribute("startDate"));
			session.setAttribute("endDate", request.getAttribute("startDate"));
			
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
		String currentUser = (String) session.getAttribute("guestEmailName");

		try 
		{
			
			int reservation_id =  Integer.parseInt(submitReservationParams[1]);
			int room_id = Integer.parseInt(submitReservationParams[0]);

			Connection con = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
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
		
		/* For billing if within the 2 days prior to startDate */
		String ccNum = "";
	    String ccExp = "";
	    String ccAddress = "";
	    
		// Checking for clerk credentials
		if (accountType.compareTo("clerk") == 0)
		{
			// sets the accountType to guest as all reservations are under guests
			String guestType = (String) "guest";
			accountType = guestType;
			// sets the currentUser to the reservationName as this is what the reservation will be under
			currentUser = (String) session.getAttribute("guestEmailName");
		}
		
		RequestDispatcher dispatcher = null;
		try 
		{
			int reservation_id =  Integer.parseInt(deleteParams);

			Connection con = null;
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			
			PreparedStatement  billingInfoQueury = con.prepareStatement("SELECT ccNum, ccExp, ccAddress FROM hotel.account WHERE email_id = ?");
			
			/* Obtain billing information from specific guest */
			String billingInfoQuery = "SELECT ccNum, ccExp, ccAddress FROM hotel.account WHERE email_id = ?";
			
			PreparedStatement biQuery = con.prepareStatement(billingInfoQuery);
			biQuery.setString(1, currentUser);
			
			ResultSet rs = biQuery.executeQuery();
			if (rs.next())
			{	
				ccNum = rs.getString("ccNum");
				ccExp = rs.getString("ccExp");
				ccAddress = rs.getString("ccAddress");
				
				session.setAttribute("ccNum", ccNum);
				session.setAttribute("ccExp", ccExp);
				session.setAttribute("ccAddress", ccAddress);
			}
			
			/* Getting cancellation date */
			LocalDate localDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String deleteDate = localDate.format(formatter);
		    
			/* Getting access to year, month, day for delete date */
			String[] deleteDateData = deleteDate.split("-");
			
			/* Getting start date information */
			String startDate = "";
			boolean isCheckedIn = false;
			
			PreparedStatement startDateQuery = con.prepareStatement("SELECT startDate, isCheckedIn FROM reservation WHERE reservationID=?");
			startDateQuery.setInt(1, reservation_id);
			rs = startDateQuery.executeQuery();
			if (rs.next())
			{	
				startDate = rs.getString("startDate");
				isCheckedIn = (rs.getInt("isCheckedIn") == 1) ? true : false;
			}
			
			/* Getting access to year, month, day for start date */
			String[] startDateData = startDate.split("-");
					
			/* Year is first index, month is second index, day is third index */
			int year = 0;
			int month = 1;
			int day = 2;
			
			boolean issueCancellationFee = false;
			
			/* Check if cancellation date falls within 2 days of reservation start date */
			if (deleteDateData[year].compareTo(startDateData[year]) == 0 && 
				deleteDateData[month].compareTo(startDateData[month]) == 0 &&
				Integer.valueOf(startDateData[day]) - Integer.valueOf(deleteDateData[day]) <= 2)
			{
				issueCancellationFee = true;
			}
	
			/* Missing billing information from guest */
			if (ccNum.compareTo("") == 0 || ccExp.compareTo("") == 0 || ccAddress.compareTo("") == 0)
			{
				dispatcher = request.getRequestDispatcher("accountbillingClerk.jsp");
				dispatcher.forward(request, response);
			}
			/* Not missing billing information, continue delete */
			else 
			{
				if (isCheckedIn == false)
				{
					if (issueCancellationFee)
					{
						String costQuery = "SELECT 0.80 * (CASE rm.Quality "
					                + "                    WHEN 'Excellent' THEN 150 "
					                + "                    WHEN 'Good' THEN 100 "
					                + "                    WHEN 'Average' THEN 80 "
					                + "                    WHEN 'Poor' THEN 50 "
					                + "                    ELSE 0 "
					                + "                    END) as amountDue "
					                + "FROM reservation r JOIN rooms rm USING(id) WHERE r.reservationID = ?";
						PreparedStatement cancellationFee = con.prepareStatement(costQuery);
						cancellationFee.setInt(1, reservation_id);
						
						rs = cancellationFee.executeQuery();
						if (rs.next())
						{
							session.setAttribute("cancellationFee", rs.getString("amountDue"));
						}
						
						dispatcher = request.getRequestDispatcher("cancellationFeeClerk.jsp");
						dispatcher.forward(request, response);
					}
					
					// Delete statement
					PreparedStatement reservationDelete = con.prepareStatement("DELETE FROM reservation WHERE reservationID = ?;");
					
					// setting wildcards in the statement to its respective value
					reservationDelete.setInt(1, reservation_id);
					
					// executing the update
					int rowsDeleted = reservationDelete.executeUpdate();
				}
				
				// Calling the clerk's reservation view
				if (issueCancellationFee == false)
				{
					clerkViewReservations(request, response);
				}
				
				// For Unit Testing
				request.setAttribute("status", "success");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}