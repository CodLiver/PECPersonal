
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="ejbholidaybookingapp.EmployeeDTO"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="css/bootstrap_3.7.css">
</head>

<body>
	<div>
		<%
		String isLogin = (String) session.getAttribute("username");
		if (isLogin != "admin") {
			response.sendRedirect("HolidayBookingAppServlet");
			//response.sendRedirect("/login.jsp");
		}
	%>
		<div class="row">
			<div class="col-md-6">
				<h3>Employees</h3>
			</div>
		</div>

		<div>
			<a href="NewEmployeeServlet"><button class="btn btn-warning">
					Add New Employee</button></a>
		</div>
	</div>
	<table class="table table-bordered table-striper table-hover">
		<tr>
			<th>ID</th>
			<th>First_name</th>
			<th>Last_name</th>
			<th>Email</th>
			<th>Department</th>
			<th>Total Holiday</th>
			<th class="text-center">Actions</th>
		</tr>
		<%
			List<EmployeeDTO> allEmployees = (List) request.getAttribute("allEmployees");
			for (EmployeeDTO e : allEmployees) {
		%>
		<tr>
			<td><%=e.getId()%></td>
			<td><%=e.getFirstName()%></td>
			<td><%=e.getLastName()%></td>
			<td><%=e.getEmail()%></td>
			<td><%=e.getDepName()%></td>
			<td><%=e.getHoliday_entitlement()%></td>
			<td><a href="EditEmployeeServlet?id=<%=e.getId()%>"><button
						class="btn btn-warning">Edit</button></a></td>
			<td></td>
			<td><a href="DeleteEmployeeServlet?id=<%=e.getId()%>"><button
						class="btn btn-warning">Delete</button></a></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>
