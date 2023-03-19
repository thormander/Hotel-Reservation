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
	<h1>Clerk dashboard</h1>
	<a href="roomList.jsp">Access Room List</a>


	<form action="accountHandler" method="post">
		<input type="hidden" name="accountType" value="logout">
  		<button type="submit">Logout</button>
	</form>
</body>




</html>
