package tests.reservation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import com.hotel.reservation.ReservationHandler;
import com.hotel.reservation.Room;


class Start_Reservation {

	@Test
	public void testStartReservation() throws ServletException, IOException, SQLException {

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
        HttpSession session = Mockito.mock(HttpSession.class);

	    String startDate = "2021-01-01";
	    String endDate = "2021-01-05";
	    String smoking = "no";
	    String bAmount = "2";
	    String bType = "queen";
	    String quality = "high";
	    String dates = "";

	    Mockito.when(request.getParameter("startDate")).thenReturn(startDate);
	    Mockito.when(request.getParameter("endDate")).thenReturn(endDate);
	    Mockito.when(request.getParameter("smoking")).thenReturn(smoking);
	    Mockito.when(request.getParameter("amountBeds")).thenReturn(bAmount);
	    Mockito.when(request.getParameter("bedType")).thenReturn(bType);
	    Mockito.when(request.getParameter("quality")).thenReturn(quality);

	    Mockito.when(request.getSession()).thenReturn(session);
	    Mockito.when(request.getRequestDispatcher("availableRoomList.jsp")).thenReturn(dispatcher);

	    Connection con = mock(Connection.class);
	    PreparedStatement pst = mock(PreparedStatement.class);
	    ResultSet rs = mock(ResultSet.class);
	    ArrayList<Room> myList = new ArrayList<>();

	    Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
	    Mockito.when(rs.getInt("id")).thenReturn(1);
	    Mockito.when(rs.getString("amountBeds")).thenReturn(bAmount);
	    Mockito.when(rs.getString("bedSize")).thenReturn(bType);
	    Mockito.when(rs.getString("smoking")).thenReturn(smoking);
	    Mockito.when(rs.getString("Quality")).thenReturn(quality);
	    Mockito.when(rs.getString("roomInformation")).thenReturn("This is a test room");

	    Mockito.when(con.prepareStatement(any(String.class))).thenReturn(pst);
	    Mockito.when(pst.executeQuery()).thenReturn(rs);

	    Mockito.when(session.getAttribute("startDate")).thenReturn(startDate);
	    Mockito.when(session.getAttribute("endDate")).thenReturn(endDate);
	    
	    ReservationHandler reservationHandler = new ReservationHandler();
	    reservationHandler.StartReservation(request, response);

        // verify that the status attribute was set to "success"
        Mockito.verify(request).setAttribute(Mockito.eq("status"), Mockito.eq("success"));
	}

}
