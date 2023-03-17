<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Sign Up Form</title>

<script>
function showErrorMessage() {
    var errorMessage = '<%= request.getAttribute("errorMessage") %>';
    if (errorMessage) {
        alert(errorMessage);
    }
}
</script>

</head>
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

<input type="hidden" id="status" value="<%= request.getAttribute("status") %>">

	<div class="main">

		<!-- Sign up form -->
		<section class="signup">
			<div class="container">
				<div class="signup-content">
					<div class="signup-form">
						<h2 class="form-title">Guest Account Creation</h2>
						<form method="post" action="register" class="register-form" id="register-form">
							<div class="form-group">
								<label for="name"><i
									class="zmdi zmdi-account material-icons-name"></i></label> <input
									type="text" name="name" id="name" placeholder="Your Name" required="required"/>
							</div>
							<div class="form-group">
								<label for="email"><i class="zmdi zmdi-email"></i></label> <input
									type="email" name="email" id="email" placeholder="Your Email" required="required"/>
							</div>
							<div class="form-group">
								<label for="pass"><i class="zmdi zmdi-lock"></i></label> <input
									type="password" name="pass" id="pass" placeholder="Password" required="required"/>
							</div>
							<div class="form-group form-button">
								<input type="submit" name="signup" id="signup"
									class="form-submit" value="Register" />
							</div>
						</form>
					</div>
					<div class="signup-image">
						<a href="login.jsp" class="signup-image-link">Already have an account?</a>
					</div>
				</div>
			</div>
		</section>
	</div>
	
</body>
</html>