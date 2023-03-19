<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>


<html>
<head>
<title>Create Reservation</title>
<style>
table 
{
  border-collapse: collapse;
  width: 100%;
}

tr ]
{
  border-bottom: 1px solid #ddd;
}
</style>
</head>
<body>


	<header>
			<h1>Input Reservation Characteristics:</h1>
	</header>
	
	<div>
		<div>
			<div>
				<form method="post" action="reservationHandler">
					<input type="hidden" name="reservationStep" value="startReservation">
				
					<div>
						<h2>Start Date:</h2>
						<label for="startDate"></label> <input
							type="date" name="startDate" id="startDate" placeholder="Start Date" required="required"/>
					</div>
					
					<div class="form-group">
						<h2>End Date:</h2>
						<label for="endDate"></label> <input
							type="date" name="endDate" id="endDate" placeholder= "End Date" required="required"/>
					</div>
					
					<div class="form-group">
						<label for="smoking">Smoking:</label>
						<select name="smoking" id="smoking">
						  <option value="Yes">Yes</option>
						  <option value="No">No</option>
						</select>
					</div>
					
					<div class="form-group">
						<label for="amountBeds">Amount of beds:</label>
						<select name="amountBeds" id="amountBeds">
						  <option value="1">1</option>
						  <option value="2">2</option>
						  <option value="5">5</option>
						</select>
					</div>
				
					<div class="form-group">
						<label for="bedType">Bed Type:</label>
						<select name="bedType" id="bedType">
						  <option value="Queen">Queen</option>						  
						  <option value="King">King</option>
						  <option value="Twin">Twin</option>
						  <option value="Full">Full</option>
						</select>
					</div>						

					<div class="form-group">
						<label for="quality">Room Quality:</label>
						<select name="quality" id="quality">
						  <option value="Excellent">Excellent</option>						  
						  <option value="Good">Good</option>
						  <option value="Average">Average</option>
						  <option value="Poor">Poor</option>
						</select>
					</div>					
					

					<div class="form-group form-button">
						<input type="submit" name="signup" id="signup"
									class="form-submit" value="Search Rooms" />
					</div>
					
				</form>
			</div>
		</div>
	</div>
</body>
</html>