package tests.account;

import java.util.List;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.junit.jupiter.api.Test;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Modify_Clerk {

    @Test
    public void testModifyClerk_SuccessfulModify() throws Exception {
        // Create a new HttpClient instance
        HttpClient httpClient = HttpClients.createDefault();

        // URL pathing as if you were on the webpage
        String url = "http://localhost:8080/hotelMain/accountHandler";

        // Set the request parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("accountType", "modifyClerk"));
        params.add(new BasicNameValuePair("pass", "new"));
        params.add(new BasicNameValuePair("name", "Firstname Lastname"));
        params.add(new BasicNameValuePair("email", "testClerkModify@test.com"));
        params.add(new BasicNameValuePair("passOld", "old"));
        
        // Build the URI with query parameters
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameters(params);
        
        // HTTP GET request
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        HttpResponse response = httpClient.execute(httpGet);
        
        // Check success conditions
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

}

/*ON SUCCESS: Function is being properly executed and passed through
 *ON FAILURE: Function broke out of try loop due to a error
 * */