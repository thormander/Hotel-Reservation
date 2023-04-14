package tests.account;

import java.util.List;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.NameValuePair;
import org.junit.jupiter.api.Test;
import org.apache.http.client.methods.HttpPost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Register_Guest {

    @Test
    public void testRegisterGuest_SuccessfulRegistration() throws Exception {
        // Create a new HttpClient instance
        HttpClient httpClient = HttpClients.createDefault();

        // URL pathing as if you were on the webpage
        String url = "http://localhost:8080/hotelMain/accountHandler";

        // HTTP POST request
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // Set the request parameters for a successful registration
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("accountType", "registerGuest"));
        params.add(new BasicNameValuePair("name", "Firstname Lastname"));
        params.add(new BasicNameValuePair("email", "unitTestGuest@test.com"));
        params.add(new BasicNameValuePair("pass", "passwordTest"));
        params.add(new BasicNameValuePair("testMode", "true"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());

        // Check success conditions
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(responseBody.contains("success"));
    }
}

/*
 * ON SUCCESS: it will insert the test case into the database
 * ON FAILURE: it means the given email is already in the database
 */

// THIS WILL INSERT INTO THE DATABASE. YOU CAN LEAVE IT IN TO TEST TO MAKE SURE IT WONT INSERT (this will result in a failure of this test case)