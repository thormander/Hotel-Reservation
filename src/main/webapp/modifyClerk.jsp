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

	<div class="main">

		<!-- Sign up form -->
		<section class="signup">
			<div class="container">
				<div class="signup-content">
					<div class="signup-form">
						<h2 class="form-title">Modify Account</h2>
						<form method="post" action="modifyClerk">
							<div class="form-group">
								<input type="text" name="name" id="nameFull" placeholder="Your Name" required="required"/>
							</div>
							<div class="form-group">
								<input type="email" name="email" id="userEmail" placeholder="Your Email" required="required"/>
							</div>
							<div class="form-group">
								<input type="password" name="passOld" id="pass" placeholder="Old Password" required="required"/>
							</div>								
							<div class="form-group">
								<input type="password" name="pass" id="pass" placeholder="New Password" required="required"/>
							</div>
							<div class="form-group form-button">
								<input type="submit" name="signup" id="signup"
									class="form-submit" value="Modify" />
							</div>
						</form>
					</div>
					<div>
						<a href="indexClerk.jsp">Back to dashboard?</a>
					</div>
				</div>
			</div>
		</section>


	</div>
</body>
</html>
