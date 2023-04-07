<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Coding Ninja's Hotel</title>
</head>
<body>
    <h1>Generate Bill</h1>

    <%
        String guestType = (String) request.getAttribute("guestType");
        double amountDue = (Double) request.getAttribute("amountDue");
        String employer = (String) request.getAttribute("employer");
    %>

    <h2><%= "corpo".equals(guestType) ? "Company Bill" : "Guest Bill" %></h2>
    <% if ("corpo".equals(guestType)) { %>
        <p><%= employer %> will be billed: <%= amountDue %></p>
    <% } else { %>
        <p>Amount Due for Guest: <%= amountDue %></p>
    <% } %>
    
    <a href="javascript:history.back()">Back to Checkout List</a>
</body>
</html>