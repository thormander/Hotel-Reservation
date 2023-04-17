<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Login Form</title>


</head>
<body>

<input type="hidden" id="status" value="<%= request.getAttribute("status") %>">
	<div class="main">
	<a href="login.jsp">Back to Main Login?</a>

		<!-- Sign in  Form -->
		<section class="sign-in">
			<div class="container">
				<div class="signin-content">
					<div class="signin-form">
						<h2 class="form-title">Clerk Sign in</h2>
						<form method="post" action="accountHandler" class="register-form" id="login-form">
							<input type="hidden" name="accountType" value="clerk">
							<div class="form-group">
								<label for="username"><i
									class="zmdi zmdi-account material-icons-name"></i></label> <input
									type="text" name="username" id="username"
									placeholder="Your Email" required="required"/>
							</div>
							<div class="form-group">
								<label for="password"><i class="zmdi zmdi-lock"></i></label> <input
									type="password" name="password" id="password"
									placeholder="Password" required="required"/>
							</div>
							<div class="form-group form-button">
								<input type="submit" name="signin" id="signin"
									class="form-submit" value="Log in" />
							</div>
						</form>
						<a href="login.jsp">Login as Guest</a>
						<a href="loginAdmin.jsp">Login as Admin</a>
					</div>
				</div>
			</div>
		</section>

	</div>

</body>
</html>