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

	<div>
		<section class="signup">
			<div class="container">
				<div class="signup-content">
					<div class="signup-form">
						<h1 class="form-title">Modify Reservation</h1>
						<%
						  String warning = (String) request.getAttribute("warning");						 
						  if (warning == null || warning == "") {%>
						    <p><b></b></p>
						   <%
						  } else {%>
							  <p><b><%= warning %></b></p>
						 <% }
						%>					
						<form method="post" action="reservationHandler">
							<div class="form-group">
								<p><b>Start Date:</b><input type="date" name="startDate" id="startDate" placeholder="Start Date"/></p>
							</div>
							<div class="form-group">
								<p><b>End Date:</b><input type="date" name="endDate" id="endDate" placeholder="End Date"/></p>
								
							</div>
							<div class="form-group form-button">
								<button type="submit" name="submitReservation" value="${reservationId}">Submit Changes</button>
							</div>
							<div>
						<button type="submit" name="myReservations" value="${sessionScope.email}">Back to My Reservations</button>
							</div>
						</form>
					</div>
					
				</div>
			</div>
		</section>


	</div>
</body>
</html>
