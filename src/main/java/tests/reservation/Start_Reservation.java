package tests.reservation;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class Start_Reservation {

	@Test
	public void testStartReservation() throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        String url = "http://localhost:8080/hotelMain/reservationHandler";

        // Create URI with query parameters
        URI uri = new URIBuilder(url)
                .addParameter("startDate", "2024-01-01")
                .addParameter("endDate", "2024-01-05")
                .addParameter("smoking", "no")
                .addParameter("amountBeds", "2")
                .addParameter("bedType", "queen")
                .addParameter("quality", "high")
                .build();

        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("reservationAction", "startReservation"));
        params.add(new BasicNameValuePair("testMode", "true"));
        
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        
        // Check success conditions
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(responseBody.contains("success"));
	}

}
