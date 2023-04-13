package tests.account;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.apache.http.client.methods.HttpGet;

import static org.junit.jupiter.api.Assertions.assertTrue;

class Login_Admin {

    @Test
    public void testAdminSignIn_SuccessfulSignIn() throws Exception {
        // Create a new HttpClient instance
        HttpClient httpClient = HttpClients.createDefault();

        // URL pathing as if you were on the webpage
        String url = "http://localhost:8080/hotelMain/accountHandler?username=admin@test.com&password=admin&accountType=admin";

        // HTTP GET request
        HttpGet httpGet = new HttpGet(url);

        HttpResponse response = httpClient.execute(httpGet);
        String responseBody = EntityUtils.toString(response.getEntity());

        // Check for the unique string to that webpage
        assertTrue(response.getStatusLine().getStatusCode() == 200);
        assertTrue(responseBody.contains("Welcome Admin!"));
    }
}

/* ON SUCCESS: this means test case received the 'success' from the method for a normal execution of the function (and user is present on database)
 * ON FAIL: means the login credentials were not in the correct format
 * */