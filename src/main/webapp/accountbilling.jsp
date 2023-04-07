<%
	if(session.getAttribute("name")==null)
	{
		response.sendRedirect("login.jsp");
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
<a href="index.jsp">Back to account home?</a>
	<br>
	<div>
		<div>
			<div>
				
				<form action="accountHandler" method="post">
					<input type="hidden" name="accountType" value="modifyBilling">
					<input type="text" name="creditCardNum" id="creditCardNum" placeholder="Credit Card Number"/>
					<input type="text" name="creditCardExp" id="creditCardExp" placeholder="Expiration"/>
					<input type="text" name="creditCardAddress" id="creditCardAddress" placeholder="Billing Address"/></br>
					
					<label><input type="checkbox" name="billingCompany" id="billingCompany" onchange="toggleBillingFields(); toggleCompanyField();">Company Billing?</label></br>
					
			         <div id="companyField" style="display: none;">
			         	<input type="text" name="companyName" id="companyName" placeholder="Company Name" />
			         </div>
				<button type="submit">Save</button>
				</form>
			</div>
		</div>
	</div>
	
	<script> //To handle dimming of other fields when paying by company
		function toggleBillingFields() {
			var checkbox = document.getElementById("billingCompany");
			var creditCardNumField = document.getElementById("creditCardNum");
			var creditCardExpField = document.getElementById("creditCardExp");
			var creditCardAddressField = document.getElementById("creditCardAddress");
			
			if (checkbox.checked) {
				creditCardNumField.disabled = true;
				creditCardExpField.disabled = true;
				creditCardAddressField.disabled = true;
			} else {
				creditCardNumField.disabled = false;
				creditCardExpField.disabled = false;
				creditCardAddressField.disabled = false;
			}
		}
	    function toggleCompanyField() {
	        var companyFieldDiv = document.getElementById("companyField");
	        if (document.getElementById("billingCompany").checked) {
	          companyFieldDiv.style.display = "block";
	        } else {
	          companyFieldDiv.style.display = "none";
	        }
	      }
	</script>
</body>
</html>