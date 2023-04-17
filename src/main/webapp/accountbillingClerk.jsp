<%
	if(session.getAttribute("name")==null)
	{
		response.sendRedirect("loginClerk.jsp");
	}
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<title>Coding Ninja's Hotel</title>


<body>
	<h1>Guest Billing Information</h1>
	
	<table border = "1">
		<thead>
			<tr>
				<th>Credit Card Number</th>
				<th>Credit Card Expiration Date</th>
				<th>Credit Card Billing Address</th>
			</tr>
		</thead>
		
		<tbody>
				<tr>
					<td><c:out value="${ccNum}"/></td>
					<td><c:out value="${ccExp}"/></td>
					<td><c:out value="${ccAddress}"/></td>
				</tr>
		</tbody>
	</table>

	<br>
	<div>
		<div>
			<div>
				
				<form action="accountHandler" method="post">
					<input type="hidden" name="accountType" value="modifyBilling">
					<input type="text" name="creditCardNum" id="creditCardNum" placeholder="Credit Card Number"/>
					<input type="text" name="creditCardExp" id="creditCardExp" placeholder="Expiration"/>
					<input type="text" name="creditCardAddress" id="creditCardAddress" placeholder="Billing Address"/></br>
				<button type="submit">Save</button>
				</form>
			</div>
		</div>
	</div>
	
</body>
<a href="indexClerk.jsp">Back to account home?</a>
</html>