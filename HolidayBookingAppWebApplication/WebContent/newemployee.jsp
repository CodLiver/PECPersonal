<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="ejbholidaybookingapp.DepartmentDTO"%>
<%@ page import="ejbholidaybookingapp.EmployeeRoleDTO"%>
<!DOCTYPE html>
<html>
<head>
<title>Add new Employee</title>
<link rel="stylesheet" href="css/bootstrap_3.7.css">
</head>

<body>
	<%
		String isLogin = (String) session.getAttribute("username");
		if (isLogin != "admin") {
			response.sendRedirect("HolidayBookingAppServlet");
		}
		%>
	<div class="row">
		<div class="col-md-6">
			<h3>New employee</h3>
		</div>
	</div>
	<form method="POST" action="NewEmployeeServlet">
		<div class="form-group">
			<label>First_name</label> <input class="form-control"
				name="firstName" placeholder="First_name" />
		</div>

		<div class="form-group">
			<label>Last_name</label> <input class="form-control" name="lastName"
				placeholder="Last_name" />
		</div>
		<div class="form-group">
			<label>Email</label> <input class="form-control" name="email"
				placeholder="email" />
		</div>
		<div class="form-group">
			<label>Phone_number</label> <input class="form-control"
				name="phoneNumber" placeholder="Phone_number" />
		</div>
		<div class="form-group">
			<label>Home_address</label> <input class="form-control"
				name="homeAddress" placeholder="Home_address" />
		</div>
		<div class="form-group">
			<label>Hire_date</label> <input class="form-control" name="hireDate"
				placeholder="Hire_date" />
		</div>
		<div class="form-group">
			<label>Salary</label> <input class="form-control" name="salary"
				placeholder="Salary" />
		</div>
		<div class="form-group">
			<label>Password</label> <input class="form-control" name="password"
				placeholder="password" />
		</div>

		<div class="form-group">
			<label>Select a Department</label> <select
				class="select form-control" name="selectedEmployeeDepartment">
				<option class="form-control" value="" selected="selected"
					disabled="disabled">Select an department:</option>
				<%
							List<DepartmentDTO> departments = (List) request.getAttribute("departments");
							for (DepartmentDTO e : departments) {
						%>
				<option class="form-control" value="<%=e.getIdDep()%>"><%=e.getDepartmentName()%></option>
				<%
							}
						%>
			</select>
		</div>

		<div class="form-group">
			<label>Select a Role</label> <select class="select form-control"
				name="selectedEmployeeRole">
				<option class="form-control" value="" selected="selected"
					disabled="disabled">Select a role:</option>
				<%
							List<EmployeeRoleDTO> employeeRoles = (List) request.getAttribute("employeeRoles");
							for (EmployeeRoleDTO e : employeeRoles) {
						%>
				<option class="form-control" value="<%=e.getId()%>"><%=e.getRoleName()%></option>
				<%
							}
						%>
			</select>
		</div>

		<button type="submit">Submit</button>
		<a href="EmployeesServlet">Back</a>
	</form>
</body>
</html>
