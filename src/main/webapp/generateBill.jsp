<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Coding Ninja's Hotel</title>
<script>
    function toggleCreditCardInfo() {
        var checkBox = document.getElementById("storedBillingInfo");
        var creditCardInfo = document.getElementById("creditCardInfo");
        creditCardInfo.style.display = checkBox.checked ? "none" : "block";
    }
</script>
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

    <% if (!"corpo".equals(guestType)) { %>
        <form action="your_payment_processing_servlet" method="POST">
            <input type="checkbox" id="storedBillingInfo" name="storedBillingInfo" onchange="toggleCreditCardInfo()">
            <label for="storedBillingInfo">Use stored billing information</label><br><br>

            <div id="creditCardInfo">
                <label for="cardNumber">Card Number:</label>
                <input type="text" id="ccNumInput" name="ccNumInput"><br><br>

                <label for="expirationDate">Expiration Date:</label>
                <input type="text" id="ccExpInput" name="ccExpInput"><br><br>
                
                <label>Address:</label>
                <input type="text" id="ccAddInput" name="ccExpInput"><br><br>

            </div>

            <input type="submit" value="Pay Bill">
        </form>
    <% } %>
    
    <a href="javascript:history.back()">Back to Checkout List</a>
</body>
</html>