package tests.reservation;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Create_Reservation {
    
	@Test
    public void testCreateReservation() throws Exception, ClassNotFoundException {
        // Create a new HttpClient instance
        HttpClient httpClient = HttpClients.createDefault();

        // URL pathing as if you were on the webpage
        String url = "http://localhost:8080/hotelMain/reservationHandler";

        // HTTP POST request
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // Set the request parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("reservationStep", "createReservation"));
        params.add(new BasicNameValuePair("roomId", "9999"));
        params.add(new BasicNameValuePair("startDate", "9999-04-10"));
        params.add(new BasicNameValuePair("endDate", "9999-04-15"));
        params.add(new BasicNameValuePair("email", "junitTest@test.com"));
        params.add(new BasicNameValuePair("type", "junittest"));
        params.add(new BasicNameValuePair("testMode", "true"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        
        // Check success conditions
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(responseBody.contains("success"));

    }	

}

/* NOTE: this will not properly filter a reservation (searchReservation handles filtering)
 * ON SUCCESS: add a test case reservation to database
 * */
