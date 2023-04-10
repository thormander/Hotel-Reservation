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
<style>
table 
{
  border-collapse: collapse;
  width: 80%;
}
tr ]
{
  border-bottom: 1px solid #ddd;
}
</style>
</head>
<!------------------------------------------------------------------------------------------------------------------->
<body>
	
	<h1>Bill Summary</h1>
		
			<table border = "1">
				<thead>
					<tr>
						<th>User</th>
						<th>Room ID</th>
						<th>Check In Date</th>
						<th>StartDate</th>
						<th>End Date</th>
						<th>Duration of Stay</th>
						<th>Cost</th>
					
					</tr>
				</thead>
				<tbody>
					<c:forEach var="bill" items="${billSummary}">
						
						<tr>
							<td><c:out value="${bill.email}" /></td>
                             <td><c:out value="${bill.roomID}"/></td>
                             <td><c:out value="${bill.checkInDate}" /></td>
                             <td><c:out value="${bill.startDate}"/></td>
                             <td><c:out value="${bill.endDate}" /></td>
                             <td><c:out value="${bill.duration}" /></td>
                           	 <td><c:out value="${bill.costOfStay}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
		<a href="indexClerk.jsp">Back to clerk home</a>
</body>





</html>