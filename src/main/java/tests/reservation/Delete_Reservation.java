package tests.reservation;

import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.hotel.reservation.ReservationHandler;

public class Delete_Reservation {


    @Test
    public void testDeleteReservations() throws Exception {
    	// Mock required objects
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        // Set up mock behavior
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("email")).thenReturn("test@test.com");
        Mockito.when(session.getAttribute("type")).thenReturn("user");
        Mockito.when(request.getRequestDispatcher("viewReservations.jsp")).thenReturn(dispatcher);
        Mockito.when(request.getParameter("deleteParams")).thenReturn("1");

  
        
	    ReservationHandler reservationHandler = new ReservationHandler();
	    reservationHandler.deleteReservations(request, response, "1");

        // verify that the status attribute was set to "success"
        Mockito.verify(request).setAttribute(Mockito.eq("status"), Mockito.eq("success"));
    }
}
