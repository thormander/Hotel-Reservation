<%
	if(session.getAttribute("name")==null)
	{
		response.sendRedirect("loginAdmin.jsp");
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

<script>
function showErrorMessage() {
    var errorMessage = '<%= request.getAttribute("errorMessage") %>';
    if (errorMessage) {
        alert(errorMessage);
    }
}
</script>

</head>
<!------------------------------------------------------------------------------------------------------------------->
<body>

  <%
    String showError = (String) request.getAttribute("showError");
    if (showError != null && showError.equals("true")) {
  %>
      <script>
        showErrorMessage();
      </script>
  <%
    }
  %>
 
<h1>Welcome Admin!</h1>

		<!-- Sign up form -->
		<section class="signup">
			<div class="container">
				<div class="signup-content">
					<div class="signup-form">
						<h2 class="form-title">Clerk Account Creation</h2>

						<form method="post" action="accountHandler" class="register-form" id="register-form">
							<input type="hidden" name="accountType" value="registerClerk">
							<div class="form-group">
								<label for="name"><i
									class="zmdi zmdi-account material-icons-name"></i></label> <input
									type="text" name="name" id="name" placeholder="Clerk Name" required="required"/>
							</div>							
							<div class="form-group">
								<label for="email"><i class="zmdi zmdi-email"></i></label> <input
									type="email" name="email" id="email" placeholder="Clerk Email" required="required"/>
							</div>
							<div class="form-group">
								<label for="pass"><i class="zmdi zmdi-lock"></i></label> <input
									type="password" name="pass" id="pass" placeholder="Clerk Password" required="required"/>
							</div>
							<div class="form-group form-button">
								<input type="submit" class="form-submit" value="Create" />
							</div>
						</form>
					</div>
				</div>
			</div>
		</section>
	<form action="accountHandler" method="post">
		<input type="hidden" name="accountType" value="logout">
  		<button type="submit">Logout</button>
	</form>
</body>
</html>
