<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>

<jsp:include page="/roomHandler"/>
<html>
<head>
<title>Room Management Application</title>
<style>
table 
{
  border-collapse: collapse;
  width: 100%;
}

tr ]
{
  border-bottom: 1px solid #ddd;
}
</style>
</head>
<body>


	<header>
			<h1>Room Management:</h1>
	</header>
	<br>
	
<a href="indexClerk.jsp">Back to clerk dashboard?</a>
	
	<div class="row">
	
		<div class="container">
			<h3 class="text-center">List of Rooms</h3>
			<hr>
			<div class="container text-left">
			</div>
			<br>
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
					<c:forEach var="room" items="${allRooms}">
						<tr>
							<td><c:out value="${room.id}" /></td>
							<td><c:out value="${room.bedSize}" /></td>
							<td><c:out value="${room.smoking}" /></td>
							<td><c:out value="${room.amountBeds}" /></td>
							<td><c:out value="${room.quality}" /></td>
							<td><c:out value="${room.roomInformation}" /></td>
							<td><a href="edit?id=<c:out value='${room.id}' />">Edit</a>
						</tr>
					</c:forEach>
		
				</tbody>
				
			</table>
	</div>
	
	
	
</body>
</html>