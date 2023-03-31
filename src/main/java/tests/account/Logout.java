package tests.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hotel.Account.AccountHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


class Logout {
    @Test
    public void testAdminSignIn_SuccessfulSignIn() throws Exception {
        // create mock request and response objects (mocking to simulate server requests/responses)
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        // set the session attribute to simulate an active session
        Mockito.when(request.getSession(false)).thenReturn(session);
        
        // mock the request dispatcher to capture the forwarded request
        Mockito.when(request.getRequestDispatcher(Mockito.anyString())).thenReturn(dispatcher); //comment this out to simulate if there was no session

        // create a new AccountHandler instance and call the ClerkSignIn method
        AccountHandler accountHandler = new AccountHandler();
        accountHandler.logout(request, response);

        // verify that the status attribute was set to "success"
        Mockito.verify(request).setAttribute(Mockito.eq("status"), Mockito.eq("success"));
    }

}
