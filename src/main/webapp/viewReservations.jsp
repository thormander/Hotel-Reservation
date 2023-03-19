<%
	if(session.getAttribute("name")==null)
	{
		response.sendRedirect("index.jsp");
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
<a href="index.jsp">Return to Account</a>
<h1>Manage Reservations</h1>
<div class="row">
	
		<div class="container">
			<h3 class="text-center">List of Reservations</h3>
			<hr/>
			<table border = "1">
				<thead>
					<tr>
						<th>Start Date</th>
						<th>End Date</th>
					</tr>
				</thead>
				<tbody>
						<c:forEach var="reservation" items="${requestScope.reservations}">
						<tr>
							<td><c:out value="${reservation.startDate}"/></td>
							<td><c:out value="${reservation.endDate}" /></td>
							<form action=reservationHandler method="post">
							<td>
							<button type="submit" name="editReservation" value="${reservation.id}">Modify</button></td>
							</form>
							<form action="reservationHandler" method="post">
							<td><button type="submit" name="deleteReservation" value="${reservation.id}">Delete</button></td>
							</form>
						</tr>
					</c:forEach>
				</tbody>
				
			</table>
	</div>
</body>
</html>