package tests.account;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.hotel.Account.AccountHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class Modify_Clerk {

    @Test
    public void testModifyClerk_SuccessfulModify() throws Exception {
        // create mock request and response objects (mocking to simulate server requests/responses)
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);

        // set the request parameters for a successful modification
        Mockito.when(request.getParameter("pass")).thenReturn("passModifyTest");
        Mockito.when(request.getParameter("name")).thenReturn("Firstname Lastname");
        Mockito.when(request.getParameter("email")).thenReturn("unitTestClerk@test.com"); //NOTE -> Even if you take out '@test.com' this will still be seen as successful as we do error checking through html
        Mockito.when(request.getParameter("passOld")).thenReturn("passwordTest");

        // mock the request dispatcher to capture the forwarded request
        Mockito.when(request.getRequestDispatcher(Mockito.anyString())).thenReturn(dispatcher);

        // create a new AccountHandler instance and call the registerClerk method
        AccountHandler accountHandler = new AccountHandler();
        accountHandler.modifyClerk(request, response);

        // verify that the status attribute was set to "success"
        Mockito.verify(request).setAttribute("status", "success");
    }

}

/*ON SUCCESS: Function is being properly executed and passed through
 *ON FAILURE: Function broke out of try loop due to a error
 * */

//It should still pass test even if given passcode is wrong