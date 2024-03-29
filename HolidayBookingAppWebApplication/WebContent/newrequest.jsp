<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="ejbholidaybookingapp.DepartmentDTO"%>
<%@ page import="ejbholidaybookingapp.EmployeeRoleDTO"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Add New Request</title>
	</head>

	<body>
		<%
		String isLogin = (String) session.getAttribute("username");
		if (isLogin != "admin" && isLogin != "employee") {
			response.sendRedirect("HolidayBookingAppServlet");
		}
		%>
		<p>New employee</p>
		<form method="POST" action="NewRequestServlet">
			<div>
				<label>Begin Date</label> <input type="date" value="2020-03-01" min="2020-03-01" name="beginDate" />
			</div>

			<div>
				<label>End Date</label> <input type="date" value="2020-03-02" min="2020-03-02" name="endDate"/>
			</div>
			
				<%
					String errMsg = (String) request.getAttribute("errorLoginMessage");
				if (errMsg != null) {
					out.print("<h5>" + errMsg + "</h5>");
				}
				
				String errMsg2 = (String) request.getAttribute("errorLoginMessage2");
				if (errMsg2 != null) {
					out.print("<h5>" + errMsg2 + "</h5>");
				}
				%>

			<button type="submit">Submit</button>
			<a href="EmployeesLoginServlet">Back</a>
		</form>
	</body>
</html>
