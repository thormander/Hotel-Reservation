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
<title>All Reservations</title>
</head>
<body>
<a href="indexClerk.jsp">Return back to dashboard</a>
<h1>Manage All Reservations</h1>
<div class="row">
	
		<div class="container">
			<h3 class="text-center">List of Reservations</h3>
			<hr/>
			<table border = "1">
				<thead>
					<tr>
					<th>Room ID</th>
						<th>Start Date</th>
						<th>End Date</th>
						<th>Room Information</th>
						<th>Check In Date</th>
					</tr>
				</thead>
				<tbody>
						<c:forEach var="reservation" items="${reservations}">
						<tr>
							<td><c:out value="${reservation.roomId}"/></td>
							<td><c:out value="${reservation.startDate}"/></td>
							<td><c:out value="${reservation.endDate}" /></td>
							<td><c:out value="${reservation.roomInformation}" /></td>
							<td><c:out value="${reservation.checkInDate}" /></td>
							<form action=reservationHandler method="post">
							<td>
								<button type="submit" name="clerkEditReservation" value="${reservation.roomId}-${reservation.id}">Modify</button></td>
							</form>
							<form action="reservationHandler" method="post">
							<td><button type="submit" name="clerkDeleteReservation" value="${reservation.id}">Delete</button></td>
							</form>
						</tr>
					</c:forEach>
				</tbody>
				
			</table>
	</div>
	<div>
		<i> Notice: Any Cancellation made within 2 days of Start Date will incur a charge (80% of one-night) </i>
	</div>
</body>
</html>