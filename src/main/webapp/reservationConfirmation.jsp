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
	<h1>Reservation Confirmed!</h1>
	
	<table border = "1">
		<thead>
			<tr>
				<th>ID</th>
				<th>Start Date</th>
				<th>End Date</th>
				<th>Reservation Name</th>
			</tr>
		</thead>
		
		<tbody>
				<tr>
					<td><c:out value="${ri.id}"/></td>
					<td><c:out value="${ri.startDate}"/></td>
					<td><c:out value="${ri.endDate}"/></td>
					<td><c:out value="${ri.reservationName}"/></td>
				</tr>
		</tbody>
	</table>
	<a href="searchRooms.jsp">Create another reservation?</a>
	<a href="index.jsp">Back to account home?</a>
</body>










</html>
