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
	<h1>Cancellation Fee Issued </h1>
	
	<h2>Billing Information</h2>
	
	<table border = "1">
		<thead>
			<tr>
				<th>Credit Card Number</th>
				<th>Credit Card Expiration Date</th>
				<th>Credit Card Billing Address</th>
				<th>Cost</th>
			</tr>
		</thead>
		
		<tbody>
				<tr>
					<td><c:out value="${ccNum}"/></td>
					<td><c:out value="${ccExp}"/></td>
					<td><c:out value="${ccAddress}"/></td>
					<td><c:out value="${cancellationFee}"/></td>
				</tr>
		</tbody>
	</table>
	<a href="index.jsp">Back to account home?</a>
</body>