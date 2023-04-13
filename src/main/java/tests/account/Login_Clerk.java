package tests.account;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.apache.http.client.methods.HttpGet;

import static org.junit.jupiter.api.Assertions.assertTrue;

class Login_Clerk {

    @Test
    public void testClerkSignIn_SuccessfulSignIn() throws Exception {
        // Create a new HttpClient instance
        HttpClient httpClient = HttpClients.createDefault();

        // URL pathing as if you were on the webpage
        String url = "http://localhost:8080/hotelMain/accountHandler?username=testclerk@gmail.com&password=test&accountType=clerk";

        // HTTP GET request
        HttpGet httpGet = new HttpGet(url);

        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());

        // Check for the unique string to that webpage
        assertTrue(response.getStatusLine().getStatusCode() == 200);
        assertTrue(responseBody.contains("Clerk Dashboard"));
    }    
}

/* ON SUCCESS: this means test case received the 'success' from the method for a normal execution of the function (and user is present on database)
 * ON FAIL: means the login credentials were not in the correct format
 * */

/* Please add these queries to the DB so the login functionality can be tested
INSERT INTO `hotel`.`account` (`user_name`, `email_id`, `password`, `type`) VALUES ('junitGuestLogin', 'loginTestGuest@test.com', 'passwordTest', 'guest');
INSERT INTO `hotel`.`account` (`user_name`, `email_id`, `password`, `type`) VALUES ('junitClerkLogin', 'loginTestClerk@test.com', 'passwordTest', 'clerk');
 */