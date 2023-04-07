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
		<form method="post" action="reservationHandler">
		<input type="hidden" name="reservationStep" value="checkOutConfirm">
			<div>
				<h3>List of Reservations for ${guestName}</h3>
				<hr/>
				<table border = "1">
					<thead>
						<tr>
						<th>Room ID</th>
						<th>Check In Date</th>
						<th>Start Date</th>
						<th>End Date</th>
						<th>Reservation Name</th>
						<th>Corporate Guest?</th>
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
								<td style="text-align: center;"><input type="checkbox" name="corporateGuest" value="corpo"/></td>
								<td><button type="submit" name="confirmCheckout" value="${reservation.id}">Checkout</button></td>
							</tr>
						</c:forEach>
					</tbody>
					
				</table>
			</div>
		</form>
</body>
</html>