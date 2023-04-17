<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Guest Login Form</title>

</head>
<body>

<input type="hidden" id="status" value="<%= request.getAttribute("status") %>">


	<div class="main">

		<!-- Sing in  Form -->
		<section class="sign-in">
			<div class="container">
				<div class="signin-content">
					<div class="signin-image">
						<a href="registration.jsp">Create a new guest account?</a>
					</div>

					<div class="signin-form">
						<h2 class="form-title">Guest Sign in</h2>
						<form method="post" action="accountHandler" class="register-form" id="login-form">
							<input type="hidden" name="accountType" value="guest">
							<div class="form-group">
								<label for="username"></label> <input
									type="text" name="username" id="username"
									placeholder="Your Email" required="required"/>
							</div>
							<div class="form-group">
								<input type="password" name="password" id="password"
									placeholder="Password" required="required"/>
							</div>
							<div>
								<input type="submit" name="signin" id="signin"
									class="form-submit" value="Log in" />
							</div>
						</form>
						<div class="admin-login">
							<a href="loginAdmin.jsp">Login as Admin</a>
							<a href="loginClerk.jsp">Login as Clerk</a>
						</div>
					</div>
				</div>
			</div>
		</section>

	</div>
</body>
</html>