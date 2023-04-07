<%
	if(session.getAttribute("name")==null)
	{
		response.sendRedirect("indexClerk.jsp");
	}
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Reservations</title>
</head>
<body>
<a href="indexClerk.jsp">Return to Clerk Dashboard</a>
<h1>Checkout</h1>
<div class="row">
	
		<div class="container">
			<h3 class="text-center">List of Reservations for ${guestName}</h3>
			<hr/>
			<table border = "1">
				<thead>
					<tr>
					<th>Room ID</th>
					<th>Check In Date</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Reservation Name</th>
					</tr>
				</thead>
				<tbody>
						<c:forEach var="reservation" items="${guestReservation}">
						
						<tr>
							<td><c:out value="${reservation.roomId}"/></td>
							<td><c:out value="${reservation.checkInDate}" /></td>
							<td><c:out value="${reservation.startDate}"/></td>
							<td><c:out value="${reservation.endDate}" /></td>
							<td><c:out value="${reservation.reservationName}" /></td>
							<td><a href="edit?id=<c:out value='${reservation.getId}' />">Edit</a>
						</tr>
					</c:forEach>
				</tbody>
				
			</table>
	</div>
</body>
</html>