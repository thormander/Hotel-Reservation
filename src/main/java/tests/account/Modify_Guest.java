package tests.account;

import java.util.List;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.apache.http.client.methods.HttpPost;

import static org.junit.jupiter.api.Assertions.assertTrue;

class Modify_Guest {

    @Test
    public void testModifyGuest_SuccessfulModify() throws Exception {
        // Create a new HttpClient instance
        HttpClient httpClient = HttpClients.createDefault();

        // URL pathing as if you were on the webpage
        String url = "http://localhost:8080/hotelMain/accountHandler";

        // HTTP POST request
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // Set the request parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("accountType", "modifyGuest"));
        params.add(new BasicNameValuePair("pass", "new"));
        params.add(new BasicNameValuePair("name", "Firstname Lastname"));
        params.add(new BasicNameValuePair("email", "testGuestModify@test.com"));
        params.add(new BasicNameValuePair("passOld", "old"));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = httpClient.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());

        // Check for success attribute
        assertTrue(response.getStatusLine().getStatusCode() == 200);
        assertTrue(responseBody.contains("Modify Account"));
    }

}
/*ON SUCCESS: Function is being properly executed and passed through
 *ON FAILURE: Function broke out of try loop due to a error
 * */