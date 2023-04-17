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
<a href="indexClerk.jsp">Back to Clerk Home</a>

	<div>
		<h2>Guest Account: <c:out value="${sessionScope.guestEmailName}"/></h2>
		<p> Current points: ${sessionScope.guestPoints}</p>
		
		<ul>
			<li><a href="searchRoomsClerk.jsp">Search Rooms?</a></li>
			<li><a href="accountbillingClerk.jsp?sessionValue=${sessionScope.guestEmailName}">Add or Modify Billing information?</a></li>
		</ul>
		
		<form action="reservationHandler" method="post">
			<input type="hidden" name="reservationAction" value="getReservations">
			<button type="submit" value="${sessionScope.guestEmailName}">Guest Reservations</button>
		</form>
		
		<form action="accountHandler" method="post">
			<input type="hidden" name="accountType" value="logout">
  			<button type="submit">Logout</button>
		</form>
	</div>

</html>