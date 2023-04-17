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

public class Delete_Reservation {

    @Test
    public void testDeleteReservations() throws Exception {
    	// Create a new HttpClient instance
    	HttpClient httpClient = HttpClients.createDefault();
        
    	//Pathing
    	String url = "http://localhost:8080/hotelMain/reservationHandler";
        
        // HTTP POST request
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("reservationAction", "deleteReservation"));
        params.add(new BasicNameValuePair("email", "junitTest@test.com"));
        params.add(new BasicNameValuePair("type", "junittest"));
        params.add(new BasicNameValuePair("deleteParams", "37")); //change ID here to match test reservation case you want to delete
        params.add(new BasicNameValuePair("testMode", "true"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        
        // Check success conditions
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(responseBody.contains("success"));
    }
}
