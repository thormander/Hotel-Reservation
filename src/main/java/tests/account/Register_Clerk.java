package tests.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hotel.Account.AccountHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class Register_Clerk {

    @Test
    public void testRegisterClerk_SuccessfulRegistration() throws Exception {
        // create mock request and response objects (mocking to simulate server requests/responses)
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);

        // set the request parameters for a successful registration
        Mockito.when(request.getParameter("name")).thenReturn("Firstname Lastname");
        Mockito.when(request.getParameter("email")).thenReturn("unitTestClerk@test.com"); //NOTE -> Even if you take out '@test.com' this will still be seen as successful as we do error checking through html
        Mockito.when(request.getParameter("pass")).thenReturn("passwordTest");

        // mock the request dispatcher to capture the forwarded request
        Mockito.when(request.getRequestDispatcher(Mockito.anyString())).thenReturn(dispatcher);

        // create a new AccountHandler instance and call the registerClerk method
        AccountHandler accountHandler = new AccountHandler();
        accountHandler.registerClerk(request, response);

        // verify that the status attribute was set to "success"
        Mockito.verify(request).setAttribute("status", "success");
    }    
}

/*
 * ON SUCCESS: it will insert the test case into the database
 * ON FAILURE: it means the given email is already in the database
 */

//THIS WILL INSERT INTO THE DATABASE. YOU CAN LEAVE IT IN TO TEST TO MAKE SURE IT WONT INSERT (this will result in a failure of this test case)