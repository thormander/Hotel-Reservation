<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<title>Coding Ninja's Hotel</title>

<!------------------------------------------------------------------------------------------------------------------->
<body>
	
	<h1>Welcome!</h1>
	
	<h1>Available Rooms:</h1>
	
		<form method="post" action="reservationHandler">
			<input type="hidden" name="reservationStep" value="createReservation">
		
			<table border = "1">
			
				<thead>
					<tr>
						<th>ID</th>
						<th>Bed Size</th>
						<th>Smoking</th>
						<th>Number of Beds</th>
						<th>Quality</th>
						<th>Room Information</th>
					
					</tr>
				</thead>
				<tbody>
					<c:forEach var="room" items="${rs}">
						
						<tr>
							<td><c:out value="${room.id}" /></td>
							<td><c:out value="${room.bedSize}" /></td>
							<td><c:out value="${room.smoking}" /></td>
							<td><c:out value="${room.amountBeds}" /></td>
							<td><c:out value="${room.quality}" /></td>
							<td><c:out value="${room.roomInformation}" /></td>
							
							
							<td><button value="${room.id}" name="roomId" class="form-submit" type="submit">confirm</button>
						</tr>
					</c:forEach>
		
				</tbody>

			</table>
			
		</form>
		<a href="searchRooms.jsp">Go back to search?</a>
</body>





</html>
