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

class Create_Reservation {
    
	@Test
    public void testCreateReservation() throws ServletException, IOException, ClassNotFoundException {

        // Mock required objects
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        // Define behavior for mocked objects
        Mockito.when(request.getParameter("roomId")).thenReturn("9999");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute("startDate")).thenReturn("9999-04-10");
        Mockito.when(session.getAttribute("endDate")).thenReturn("9999-04-15");
        Mockito.when(session.getAttribute("email")).thenReturn("junitTest@test.com");
        Mockito.when(session.getAttribute("type")).thenReturn("junittest");
        Mockito.when(request.getRequestDispatcher("reservationConfirmation.jsp")).thenReturn(dispatcher);

        
        
	    ReservationHandler reservationHandler = new ReservationHandler();
	    reservationHandler.CreateReservation(request, response);

        // verify that the status attribute was set to "success"
        Mockito.verify(request).setAttribute(Mockito.eq("status"), Mockito.eq("success"));

    }	

}

/* NOTE: this will not properly filter a reservation (searchReservation handles filtering)
 * ON SUCCESS: add a test case reservation to database
 * */
