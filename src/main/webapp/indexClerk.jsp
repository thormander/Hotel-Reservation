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
	<h1>Clerk Dashboard</h1>
	<div>
		<h2>Room List</h2>
		<form action="roomList.jsp" method="post">
			<button type="submit">View Room List</button>
		</form>
	</div>
	<div>
		<h2>View Reservations</h2>
		<form action="reservationHandler" method="post">
			<input type="hidden" name="reservationAction" value="clerkViewReservations">
			<button type="submit">View Reservations</button>
		</form>
	</div>
	<div>
		<h2>Guest Lookup</h2>
		<form action="accountHandler" method="post">
			<input type="hidden" name="accountType" value="searchGuest">
			<div>
				<input type="email" placeholder="Guest Email" name="guestEmailName"></input>
			</div>
			
			<button type="submit">Search Guest</button>
		</form>
	</div>
	
	<div>
		<h2>Checkout Guest</h2>
		<form action="reservationHandler" method="post">
			<input type="hidden" name="reservationAction" value="checkOutStart">
			<div>
				<input type="email" placeholder="Guest Email" name="emailName" required="required"></input>
			</div>
			
			<button type="submit">Checkout Guest</button>
		</form>
	</div>
	<br>
	<div>
		<form action="reservationHandler" method="post">
			<input type="hidden" name="reservationAction" value="generateBillingSummary">
			<button type="submit">Get Billing Summary</button>
		</form>
	</div>
	<br>
	<br>
	
	<a href="modifyClerk.jsp">Modify Account?</a>

	<form action="accountHandler" method="post">
		<input type="hidden" name="accountType" value="logout">
  		<button type="submit">Logout</button>
	</form>
</body>




</html>
