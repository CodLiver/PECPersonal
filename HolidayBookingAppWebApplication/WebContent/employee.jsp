
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="ejbholidaybookingapp.EmployeeDTO"%>
<!DOCTYPE html>
<html>
<body>
	<div>
	<%
		String isLogin = (String) session.getAttribute("username");
		if (isLogin != "admin") {
			response.sendRedirect("HolidayBookingAppServlet");
			//response.sendRedirect("/login.jsp");
		}
	%>
		<div>
			<h3>Employees</h3>
		</div>
		<!-- 		<form method="POST" action="NewEmployeeServlet">
			<input type="submit" value="New employee" />
		</form> -->

		<div>
			<a href="NewEmployeeServlet"> Add New Employee</a>
		</div>
	</div>
	<table>
		<tr>
			<th>ID</th>
			<th>First_name</th>
			<th>Last_name</th>
			<th>Email</th>
			<th>Department</th>
			<th>Actions</th>
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
			<td><a href="EditEmployeeServlet?id=<%=e.getId()%>">Edit</a></td>
			<td></td>
			<td><a href="DeleteEmployeeServlet?id=<%=e.getId()%>">Delete</a></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>