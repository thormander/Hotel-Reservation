package tests.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hotel.Account.AccountHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class Login_Clerk {

    @Test
    public void testClerkSignIn_SuccessfulSignIn() throws Exception {
        // create mock request and response objects (mocking to simulate server requests/responses)
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);

        // set the request parameters for a successful sign in (disclaimer: blank fields are error handled through .jsp)
        Mockito.when(request.getParameter("username")).thenReturn("unitTestClerk@test.com"); //removal of @test.com should result in failure
        Mockito.when(request.getParameter("password")).thenReturn("passwordTest");

        // mock the request dispatcher to capture the forwarded request
        Mockito.when(request.getRequestDispatcher(Mockito.anyString())).thenReturn(dispatcher);

        // create a new AccountHandler instance and call the ClerkSignIn method
        AccountHandler accountHandler = new AccountHandler();
        accountHandler.ClerkSignIn(request, response);

        // verify that the status attribute was set to "success"
        Mockito.verify(request).setAttribute(Mockito.eq("status"), Mockito.eq("success"));
    }    
}

/* ON SUCCESS: this means test case received the 'success' from the method for a normal execution of the function (and user is present on database)
 * ON FAIL: means the login credentials were not in the correct format
 * */
