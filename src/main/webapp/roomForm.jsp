<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>Room Management</title>

</head>
<body>
<a href="roomList.jsp">Back to list</a>
	<br>
	<div>
		<div>
			<div>
				
				<form action="update" method="post">
				
				

				<c:if test="${room != null}">Edit Room</c:if>

				<c:if test="${room != null}">
					<input type="hidden" name="id" value="<c:out value='${room.id}' />" />
				</c:if>

				<fieldset class="form-group">
					<label>Bed Size</label> <input type="text" value="<c:out value='${room.bedSize}' />" name="bedSize" required="required">
				</fieldset>
				
				<fieldset class="form-group">
					<label>Smoking</label> <input type="text" value="<c:out value='${room.smoking}' />" name="smoking" required="required">
				</fieldset>	
							
				<fieldset class="form-group">
					<label>Number of Beds</label> <input type="text" value="<c:out value='${room.amountBeds}' />" name="amountBeds" required="required">
				</fieldset>
				
				<fieldset class="form-group">
					<label>Quality</label> <input type="text" value="<c:out value='${room.quality}' />" name="quality" required="required">
				</fieldset>	
							
				<fieldset class="form-group">
					<label>Room Information</label> <input type="text" value="<c:out value='${room.roomInformation}' />" name="roomInformation">
				</fieldset>

				<button type="submit" class="btn btn-success">Save</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
