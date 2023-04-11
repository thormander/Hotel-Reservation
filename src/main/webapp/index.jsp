<%
	if(session.getAttribute("name")==null)
	{
		response.sendRedirect("login.jsp");
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
	<h1>Welcome!</h1>
	<p>Your current points: ${sessionScope.guestPoints}</p>
	<ul>
		<li><a href="modifyGuest.jsp">Modify Account?</a></li>
		<li><a href="accountbilling.jsp?sessionValue=${sessionScope.email}">Add or Modify Billing information?</a></li>
		<li><a href="searchRooms.jsp">Search Rooms?</a></li>
	</ul>
	<form action="reservationHandler" method="post">
		<input type="hidden" name="reservationStep" value="getReservations">
		<button type="submit" value="${sessionScope.email}">My Reservations</button>
	</form>
	<form action="accountHandler" method="post">
		<input type="hidden" name="accountType" value="logout">
  		<button type="submit">Logout</button>
	</form>
</body>










</html>
