<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Holiday Booking Application</title>
<link rel="stylesheet" href="css/bootstrap_3.7.css">
</head>
<body>
	<h1>Holiday Booking Application</h1>
	<%
		String errMsg = (String) request.getAttribute("errorLoginMessage");
	%>
	<%
		if (errMsg != null) {
			out.print("<h5>" + errMsg + "</h5>");
		}
	%>
	<%
		String isLogin = (String) session.getAttribute("username");
		if (isLogin == "admin") {
			response.sendRedirect("EmployeesServlet");
		}else if(isLogin == "employee") {
			response.sendRedirect("EmployeesLoginServlet");
		}
	%>
		<div class="col-sm-3 first_cont">
		<form class="form-signin" action="HolidayBookingAppServlet">
			<input type="text" class="form-control" name="email"
				placeholder="Email address" required>
			<p></p>
			<input type="password" class="form-control" name="password"
				placeholder="Password" required>
			<p></p>
			<button class="btn-lg btn-primary btn-block" type="submit"
				value="Login">Sign in</button>
		</form>
	</div>
</body>
</html>