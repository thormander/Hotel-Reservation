<%
	if(session.getAttribute("name")==null)
	{
		response.sendRedirect("loginClerk.jsp");
	}
%>


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
	<a href="modifyClerk.jsp">Modify Account?</a>
	<h1>Clerk Dashboard</h1>
	<a href="roomList.jsp">Access Room List</a>
	<div>
		<h3>View Reservations</h3>
		<form action="reservationHandler" method="post">
			<input type="hidden" name="reservationStep" value="clerkViewReservations">
			<button type="submit">View Reservations</button>
		</form>
	</div>
	
	
	<div>
		<h2>Checkout Guest</h2>
		<form action="reservationHandler" method="post">
			<input type="hidden" name="reservationStep" value="checkOutStart">
			<div>
				<input type="email" placeholder="Guest Email" name="emailName" required="required"></input>
			</div>
			
			<button type="submit">Checkout Guest</button>
		</form>
	</div>
	<br>
	<div>
		<form action="reservationHandler" method="post">
			<input type="hidden" name="reservationStep" value="generateBillingSummary">
			<button type="submit">Get Billing Summary</button>
		</form>
	</div>
	<br>
	<br>

	<form action="accountHandler" method="post">
		<input type="hidden" name="accountType" value="logout">
  		<button type="submit">Logout</button>
	</form>
</body>




</html>
