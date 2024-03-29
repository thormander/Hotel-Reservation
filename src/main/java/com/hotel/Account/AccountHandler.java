package com.hotel.Account;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hotel.email.EmailController;


/*
 * Servlet implementation class AccountHandler
 */
@WebServlet("/accountHandler")
public class AccountHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//doGet gets called 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//CHANGED doGet to PUBLIC as test classes needs to see it
	
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
		case "modifyAdmin": //index admin.jsp:
			System.out.println("modify admin executed!");
			modifyAdmin(request,response);
			break;
		case "modifyBilling": //accountbilling.jsp
			System.out.println("modify billing executed!");
			modifyBilling(request,response);
			break;
		case "redeemPoint":
			System.out.println("Redeemed!");
			redeemPoint(request,response);
			break;
		case "searchGuest": //searchGuest.jsp
			System.out.println("search guest executed");
			searchGuest(request,response);
			break;
		default: //default to logout (Can change to a switch case if needed)
			System.out.println("logout executed!");
			logout(request,response);
			break;
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response); //just pass to doGet...(for account handler, does not make a difference to use one or the other)
	}
	
	/*modifyBilling:
	 *	This function handles adding/modifying user billing information.
	 * */
	private void modifyBilling(HttpServletRequest request, HttpServletResponse response) {
	    String ccNum = request.getParameter("creditCardNum");
	    String ccExp = request.getParameter("creditCardExp");
	    String ccAddress = request.getParameter("creditCardAddress");
	    String billingCompany = request.getParameter("billingCompany");
	    String employer = request.getParameter("companyName");

	    HttpSession session = request.getSession();
	    String accountType = (String) session.getAttribute("type");
	    String currentUser = (String) session.getAttribute("email");
	    boolean clerkLoggedIn = false;
	    // Checking for clerk credentials
 		if (accountType.compareTo("clerk") == 0)
 		{
 			// sets the accountType to guest as all reservations are under guests
 			String guestType = (String) "guest";
 			accountType = guestType;
 			// sets the currentUser to the reservationName as this is what the reservation will be under
 			currentUser = (String) session.getAttribute("guestEmailName");
 			clerkLoggedIn = true;
 		}

	    RequestDispatcher dispatcher = null;
	    Connection con = null;

	    try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			
	        String updateQuery = "UPDATE account SET ccNum=?, ccExp=?, ccAddress=?"
	                + (billingCompany != null ? ", employer=?" : "")
	                + " WHERE email_id=?";

	        PreparedStatement modifyQuery = con.prepareStatement(updateQuery);
	        modifyQuery.setString(1, ccNum);
	        modifyQuery.setString(2, ccExp);
	        modifyQuery.setString(3, ccAddress);
	        
	        /* For displaying current billing information */
	        session.setAttribute("ccNum", ccNum);
	        session.setAttribute("ccExp", ccExp);
	        session.setAttribute("ccAddress", ccAddress);

	        if (billingCompany != null) {
	            modifyQuery.setString(4, employer);
	            modifyQuery.setString(5, currentUser);
	            
	            System.out.println("Employer Billing Address Added!");
	        } else {
	            modifyQuery.setString(4, currentUser);
	        	System.out.println("Guest Billing Address Added!");
	        }
	        modifyQuery.executeUpdate();

	        con.close();
	        
	        if (clerkLoggedIn)
	        {
	        	dispatcher = request.getRequestDispatcher("searchGuest.jsp");
	        }
	        else
	        {
	        	dispatcher = request.getRequestDispatcher("index.jsp");
	        }
	        
	        dispatcher.forward(request, response);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	/*logout:
	 *	This function logs out the user by invalidating the session and redirecting to the login page.
	 * */
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if(session != null)
		{
		    session.invalidate();
		    request.setAttribute("status", "success"); //for unit testing
		}
		request.getRequestDispatcher("/login.jsp").forward(request,response);
		
	}

	/*registerClerk:
	 *	This function registers a new clerk account by inserting the user's information into the database.
	 * */
	public void registerClerk(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("name");
		String uemail = request.getParameter("email");
		String upass = request.getParameter("pass");
		
		RequestDispatcher dispatcher = null;
		Connection con = null;
		
		boolean testMode = "true".equals(request.getParameter("testMode")); //check if request from junit
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			PreparedStatement pst = con.prepareStatement("INSERT INTO account(user_name,email_id,password,type) values(?,?,?,'clerk')");
			pst.setString(1, username);
			pst.setString(2, uemail);
			pst.setString(3, upass);
			
			int rowCount = pst.executeUpdate();
			
			dispatcher = request.getRequestDispatcher("indexAdmin.jsp");
			if (rowCount > 0)
			{
				// Write success response for unit test
		        if (testMode) {
			        PrintWriter out = response.getWriter();
			        response.setContentType("text/plain");
			        response.setCharacterEncoding("UTF-8");
			        out.write("success");
			        out.flush();
			        out.close();
		        }
				
				request.setAttribute("status", "success");
			}
			else
			{
				request.setAttribute("status", "failed");
			}
			
			//prevent forward if we are testing
			if (testMode == false) {
				dispatcher.forward(request, response);			
			}
			
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
	
	/* registerGuest:
	 * 	This function registers a new guest account by inserting the user's information into the database.
	 * */
	public void registerGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { //changed to public for junit testing
		String username = request.getParameter("name");
		String uemail = request.getParameter("email");
		String upass = request.getParameter("pass");
		
		RequestDispatcher dispatcher = null;
		Connection con = null;
		
		boolean testMode = "true".equals(request.getParameter("testMode")); //check if request from junit
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			//SQL query to database here
			PreparedStatement pst = con.prepareStatement("INSERT INTO account(user_name,email_id,password,type,points) values(?,?,?,'guest',0)");
			pst.setString(1, username);
			pst.setString(2, uemail);
			pst.setString(3, upass);
			
			int rowCount = pst.executeUpdate();
			
			dispatcher = request.getRequestDispatcher("registration.jsp");
			if (rowCount > 0)
			{
				// Write success response for unit test
		        if (testMode) {
			        PrintWriter out = response.getWriter();
			        response.setContentType("text/plain");
			        response.setCharacterEncoding("UTF-8");
			        out.write("success");
			        out.flush();
			        out.close();
		        }
		        
			    EmailController emailController = new EmailController();
			    emailController.sendRegistrationConfirmationEmail(uemail, username);
			}
			else
			{
				request.setAttribute("status", "failed");
			}
			
			if (testMode == false) {
				dispatcher.forward(request, response);
			}
			
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

	/* modifyClerk:
	 * 	This function updates the password for an existing clerk account in the database.
	 * */
	public void modifyClerk(HttpServletRequest request, HttpServletResponse response) {
		
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
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			//SQL query to database here
			
			PreparedStatement pst = con.prepareStatement("UPDATE `hotel`.`account` SET `password` = ? WHERE (`email_id` = ? AND `user_name` = ? AND `password` = ? AND `type` = 'clerk') ");
			
			pst.setString(1, upass);
			pst.setString(2, uemail);
			pst.setString(3, uname);
			pst.setString(4, upassOld);
			
			pst.executeUpdate();
			
			request.setAttribute("modifySuccess", true);			
			request.setAttribute("message", "Password modified!");
			
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

	/* modifyGuest:
	 *	This function updates the password for an existing guest account in the database.
	 * */
	public void modifyGuest(HttpServletRequest request, HttpServletResponse response) {
		
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
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			//SQL query to database here
			
			PreparedStatement pst = con.prepareStatement("UPDATE `hotel`.`account` SET `password` = ? WHERE (`email_id` = ? AND `user_name` = ? AND `password` = ? AND `type` = 'guest')");
			
			pst.setString(1, upass);
			pst.setString(2, uemail);
			pst.setString(3, uname);
			pst.setString(4, upassOld);
			
			pst.executeUpdate();
			
			request.setAttribute("modifySuccess", true);
			request.setAttribute("message", "Password modified!");
			
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

	/*AdminSignIn:
	 * 	This function handles the login process for an admin account. It verifies the user's credentials and redirects to the admin index.
	 * */
	public void AdminSignIn(HttpServletRequest request, HttpServletResponse response) {

		String uemail = request.getParameter("username");
		String upass = request.getParameter("password");
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			PreparedStatement pst = con.prepareStatement("SELECT * FROM account WHERE email_id = ? AND password = ? AND type = 'admin'");
			
			pst.setString(1, uemail);
			pst.setString(2, upass);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				request.setAttribute("status", "success"); //used for unit test to ensure it passed query
				
				session.setAttribute("name", rs.getString("user_name"));
				session.setAttribute("email", rs.getString("email_id"));
				session.setAttribute("type", rs.getString("type"));
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

	/*ClerkSignIn:
	 *	This function handles the login process for a clerk account. It verifies the user's credentials against the database and redirects to the clerk index.
	 * */
	public void ClerkSignIn(HttpServletRequest request, HttpServletResponse response) {
		String uemail = request.getParameter("username");
		String upass = request.getParameter("password");
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			PreparedStatement pst = con.prepareStatement("SELECT * FROM account WHERE email_id = ? AND password = ? AND type = 'clerk'");
			
			pst.setString(1, uemail);
			pst.setString(2, upass);
			
			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				request.setAttribute("status", "success"); //used for unit test to ensure it passed query
				
				session.setAttribute("name", rs.getString("user_name"));
				session.setAttribute("email", rs.getString("email_id"));
				session.setAttribute("type", rs.getString("type"));
				
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
	
	/* GuestSignIn:
	 * 	 This function handles the login process for a guest account. It verifies the user's credentials against the database and redirects to the guest index.
	 */
	public void GuestSignIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String uemail = request.getParameter("username");
		String upass = request.getParameter("password");
		
		/* For displaying current billing information */
		String ccNum = "";
		String ccExp = "";
		String ccAddress = "";
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			PreparedStatement pst = con.prepareStatement("SELECT * FROM account WHERE email_id = ? AND password = ? AND type = 'guest'");
			
			pst.setString(1, uemail);
			pst.setString(2, upass);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				request.setAttribute("status", "success"); //used for unit test to ensure it passed query
				
				session.setAttribute("name", rs.getString("user_name"));
				session.setAttribute("email", rs.getString("email_id"));
				session.setAttribute("type", rs.getString("type"));
				
				//Display user points on login on index
				int guestPoints = rs.getInt("points");
				request.setAttribute("guestPoints", guestPoints);
				session.setAttribute("guestPoints", guestPoints);
				
				dispatcher = request.getRequestDispatcher("index.jsp");
			}
			else
			{
				request.setAttribute("status", "failed");
				dispatcher = request.getRequestDispatcher("login.jsp");
			}
			
			/* Obtain billing information from specific guest */
			String billingInfoQuery = "SELECT ccNum, ccExp, ccAddress FROM hotel.account WHERE email_id = ?";
			
			PreparedStatement biQuery = con.prepareStatement(billingInfoQuery);
			biQuery.setString(1, uemail);
			
			rs = biQuery.executeQuery();
			if (rs.next())
			{	
				ccNum = rs.getString("ccNum");
				ccExp = rs.getString("ccExp");
				ccAddress = rs.getString("ccAddress");
			}
		
			/* For displaying current billing information */
	        session.setAttribute("ccNum", ccNum);
	        session.setAttribute("ccExp", ccExp);
	        session.setAttribute("ccAddress", ccAddress);
			
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	

	/* modifyAdmin:
	 *	This function updates the password for an existing guest account in the database.
	 * */
	public void modifyAdmin(HttpServletRequest request, HttpServletResponse response) {
		
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
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			//SQL query to database here
			
			PreparedStatement pst = con.prepareStatement("UPDATE `hotel`.`account` SET `password` = ? WHERE (`email_id` = ? AND `user_name` = ? AND `password` = ? AND `type` = 'admin')");	
			pst.setString(1, upass);
			pst.setString(2, uemail);
			pst.setString(3, uname);
			pst.setString(4, upassOld);
			
			pst.executeUpdate();
			
			request.setAttribute("status", "success"); //for unit testing
			
			dispatcher = request.getRequestDispatcher("indexAdmin.jsp");
			
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
	
	/* Search Guest 
	 * This function checks a guest entry against the guest accounts in the database
	 */
	public void searchGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uemail = request.getParameter("guestEmailName");
		
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = null;
		
		session.setAttribute("guestEmailName", uemail);
		
		String guest = (String) "guest";
		session.setAttribute("guestType", guest);
		
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			//SQL query to database here. (PLEASE NAME YOUR DB TO hotel)
			PreparedStatement pst = con.prepareStatement("SELECT * FROM account WHERE email_id = ? AND type = 'guest'");
			
			pst.setString(1, uemail);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next())
			{
				request.setAttribute("status", "success"); //used for unit test to ensure it passed query
				
				//Display user points on login on index
				int guestPoints = rs.getInt("points");
				session.setAttribute("guestPoints", guestPoints);
				
				dispatcher = request.getRequestDispatcher("searchGuest.jsp");
			}
			else
			{
				request.setAttribute("status", "failed");
				dispatcher = request.getRequestDispatcher("indexClerk.jsp");
			}
			
			dispatcher.forward(request, response);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	private void redeemPoint(HttpServletRequest request, HttpServletResponse response) {
		RequestDispatcher dispatcher = null;
		Connection con = null;
		
		try 
		{
			HttpSession session = request.getSession();
			String email = (String) session.getAttribute("email");
			

			Class.forName("com.mysql.cj.jdbc.Driver");			
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC","root", "12345678");
			//SQL query to database here
			PreparedStatement pst = con.prepareStatement("UPDATE `hotel`.`account` SET `points` = 0 WHERE `email_id` = ?");	
			request.setAttribute("guestPoints",0);
			pst.setString(1,email);
			pst.executeUpdate();
			
			dispatcher = request.getRequestDispatcher("index.jsp");
			
			dispatcher.forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
