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
	<a href="modifyGuest.jsp">Modify Account?</a>

	
	<h1>Welcome!</h1>

	<form action="accountHandler" method="post">
		<input type="hidden" name="accountType" value="logout">
  		<button type="submit">Logout</button>
	</form>
</body>










</html>
