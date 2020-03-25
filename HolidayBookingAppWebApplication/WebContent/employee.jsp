
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="ejbholidaybookingapp.EmployeeDTO"%>
<%@ page import="ejbholidaybookingapp.RequestDTO"%>
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
		<div>
			<a href="NewEmployeeServlet"><button class="btn btn-warning">
					Add New Employee</button></a>
		</div>
		<div>
			<a href="NewRequestServlet"> <button class="btn btn-warning">Submit new Request</button></a>
		</div>
		
		<div>
			<a href="RequestsAndBookingsServlet"> <button class="btn btn-warning">Show Bookings and Outstanding Requests</button></a>
		</div>
		
	</div>
	
	<div class="row">
		<div class="col-md-6">
			<h3>Personal Requests</h3>
		</div>
	</div>
	<table class="table table-bordered table-striper table-hover">
		<tr>
			<th>Begin Date</th>
			<th>End Date</th>
			<th>Duration</th>
			<th>Remaining Holiday duration</th>
			<th>Peak time?</th>
			<th>Approved?</th>
		</tr>
		<%
			String[] approved = {"Rejected", "Approved", "Pending"};
			List<RequestDTO> getAllRequestperEmp = (List) request.getAttribute("getAllRequestperEmp");
			
			for (RequestDTO r : getAllRequestperEmp) {
		%>
		<tr>
			<td><%=r.getBegin_date()%></td>
			<td><%=r.getEnd_date()%></td>
			<td><%=r.getDuration()%></td>
			<td><%=r.getHoliday_remaining()%></td>
			<td><%=r.getPeak_time()%></td>
			<td><%=approved[r.getStatus()]%></td>
			<td></td>
		</tr>
		<%
			}
		%>
	</table>
	
	
	<div class="row">
		<div class="col-md-6">
			<h3>Employees</h3>
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
