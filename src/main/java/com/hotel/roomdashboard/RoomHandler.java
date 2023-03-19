package com.hotel.roomdashboard;

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

import com.hotel.reservation.Room;



///////////////////////////////////
@WebServlet("/") //connection to the .jsp file for intake of data
public class RoomHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getServletPath();
		switch(action)
		{
		case "/edit":
			editRoom(request,response);
			break;
		
		case "/update":
			try {
				updateRoom(request,response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		default:
			List<Room> myRooms = selectAllRooms(request, response);
			request.setAttribute("allRooms", myRooms);
			break;
		}
	}

	
	private void editRoom(HttpServletRequest request, HttpServletResponse response) 
	{
		int id = Integer.parseInt(request.getParameter("id"));
		
		Rooms existingRoom;
		try
		{
			existingRoom = selectRoom(id);
			RequestDispatcher dispatcher = request.getRequestDispatcher("roomForm.jsp");
			request.setAttribute("room", existingRoom); //may need to change "room" key
			dispatcher.forward(request, response);
			
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void updateRoom(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException 
	{
		int id = Integer.parseInt(request.getParameter("id"));
		String bedSize = request.getParameter("bedSize");
		String smoking = request.getParameter("smoking");
		String amountBeds = request.getParameter("amountBeds");
		String quality = request.getParameter("quality");
		String roomInformation = request.getParameter("roomInformation");

		Rooms room = new Rooms(id,bedSize,smoking,amountBeds,quality,roomInformation);
		updateRoom(room);
		
		response.sendRedirect("roomList.jsp");
		
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~QUERY METHODS BELOW~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	//show list of rooms
	public List<Room> selectAllRooms(HttpServletRequest request, HttpServletResponse response)
	{
		Connection con = null;
		
		List<Room> myRooms = new ArrayList<>();
		System.out.println("Executed searchAllRooms!");
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			//CONNECTION TO DB (change "hotel" to whatever you database name is.)
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "1234");
			
			
			PreparedStatement pst = con.prepareStatement("Select * from rooms");

			ResultSet rs = pst.executeQuery();
	
			
			while(rs.next()) {
				Room a = new Room();
				a.setId(rs.getInt("id"));
				a.setamountBeds(rs.getString("amountBeds"));
				a.setbedSize(rs.getString("bedSize"));
				
				a.setSmoking(rs.getString("smoking"));
				a.setQuality(rs.getString("Quality"));
				a.setroomInformation(rs.getString("roomInformation"));
				myRooms.add(a);
				
			}
			

		} catch (SQLException | ClassNotFoundException e)
		{  
			e.printStackTrace();
		}
		
		return myRooms;
	}
	
	//Select one room
	public Rooms selectRoom(int id)
	{
		System.out.print("Executed selectRoom!");
		Rooms room = null;
		Connection con = null;
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false","root", "1234");
			PreparedStatement pst = con.prepareStatement("SELECT * FROM rooms WHERE id = ?");

			pst.setInt(1, id);
			
			ResultSet set = pst.executeQuery();
			System.out.println("SELECT * FROM rooms WHERE id = ?");
			
			while (set.next()) 
			{
				String bedSize = set.getString("bedSize");
				String smoking = set.getString("smoking");
				String amountBeds = set.getString("amountBeds");
				String quality = set.getString("quality");
				String roomInformation = set.getString("roomInformation");
				room = new Rooms(id,bedSize,smoking,amountBeds,quality,roomInformation);
			}

		} catch (SQLException | ClassNotFoundException e)
		{  
			e.printStackTrace();
		}
		
		return room;
	}

	//Update Room
	public boolean updateRoom(Rooms room) throws SQLException
	{
		System.out.print("Executed updateRoom!");
		boolean roomUpdated = false;
		Connection con = null;
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false","root", "1234");
			PreparedStatement pst = con.prepareStatement("UPDATE rooms SET bedSize=?,smoking=?,amountBeds=?,quality=?,roomInformation=? WHERE id=?");

			pst.setString(1, room.getBedSize());
			pst.setString(2, room.getSmoking());
			pst.setString(3, room.getAmountBeds());
			pst.setString(4, room.getQuality());
			pst.setString(5, room.getRoomInformation());
			pst.setInt(6, room.getId());
			
			roomUpdated = pst.executeUpdate() > 0;
			System.out.println("updated room!");
			
		} catch (SQLException | ClassNotFoundException e)
		{  
			e.printStackTrace();
		}
		return roomUpdated;
	}
}
