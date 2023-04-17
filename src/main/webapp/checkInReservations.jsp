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
<title>Coding Ninja's Hotel</title>
</head>
<body>
	<h1>Reservation Check-In</h1>
	
	<h2>Reservation ID: <c:out value="${myreservation.id}"/></h2>
	<h2>Guest Account: <c:out value="${myreservation.reservationName}"/></h2>
	
	
	<table border = "1">
		<thead>
			<tr>
				<th>Start Date</th>
				<th>End Date</th>
			</tr>
		</thead>
		
		<tbody>
			<tr>
				<td><c:out value="${myreservation.startDate}"/></td>
				<td><c:out value="${myreservation.endDate}"/></td>
			</tr>
		</tbody>
	</table>
	<form method="post" action="reservationHandler">
		<input type="hidden" name="reservationAction" value="checkInConfirm">
			<div>
				<h2>Check-In Date:</h2>
				<label for="checkInDate"></label> <input
					type="date" name="checkInDate" id="checkInDate" placeholder="Check-In Date"/>
			</div>
			
			<div class="form-group form-button">
				<input type="submit" name="signup" id="signup"
							class="form-submit" value="Check-In" />
			</div>
	</form>
	
	<a href="searchGuest.jsp">Return back to dashboard?</a>
</body>
</html>