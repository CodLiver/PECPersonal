<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Holiday Booking Application</title>
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
		if (isLogin != null) {
			response.sendRedirect("EmployeesServlet");
		}
	%>
	<form method="POST" action="HolidayBookingAppServlet">
		Email:<input type="text" name="email" /> Password:<input type="text"
			name="password" /> <input type="submit" value="Login" />
	</form>
</body>
</html>