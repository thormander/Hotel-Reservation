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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Create_Reservation {
    
	@Test
    public void testCreateReservation() throws Exception, ClassNotFoundException {
        
        // Check if the test reservation already exists in the database
        int existingId = checkReservationInDatabase("junitTest@test.com");
        if (existingId == 9999) {
            deleteReservationFromDatabase(existingId); //delete if it is present
        }
		
		
		// Create a new HttpClient instance
        HttpClient httpClient = HttpClients.createDefault();

        // URL pathing as if you were on the webpage
        String url = "http://localhost:8080/hotelMain/reservationHandler";

        // HTTP POST request
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // Set the request parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("reservationAction", "createReservation"));
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
        
        // Check if the reservation was inserted in the database and then remove from database
        int reservationId = checkReservationInDatabase("junitTest@test.com");
        assertTrue(reservationId == 9999);//check if test reservation was inserted
        deleteReservationFromDatabase(reservationId);
	}
	
    private int checkReservationInDatabase(String email) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		//CONNECTION TO DB (change "hotel" to whatever you database name is.)
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "12345678");
        String query = "SELECT id FROM reservation WHERE reservationName = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, email);

        ResultSet resultSet = preparedStatement.executeQuery();
        int roomIdCheck = -1;
        if (resultSet.next()) {
        	roomIdCheck = resultSet.getInt("id");
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return roomIdCheck;
    }
    private void deleteReservationFromDatabase(int roomId) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		//CONNECTION TO DB (change "hotel" to whatever you database name is.)
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel?useSSL=false&serverTimezone=UTC","root", "12345678");
        String query = "DELETE FROM reservation WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, roomId);
        preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
    }
}

/* NOTE: this will not properly filter a reservation (searchReservation handles filtering)
 * ON SUCCESS: add a test case reservation to database
 * */
