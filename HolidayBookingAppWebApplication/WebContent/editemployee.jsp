<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="ejbholidaybookingapp.EmployeeDTO"%>
<%@ page import="ejbholidaybookingapp.EmployeeRoleDTO"%>
<%@ page import="ejbholidaybookingapp.DepartmentDTO"%>
<!DOCTYPE html>
<html>
<head>
<title>Edit Employee</title>
<link rel="stylesheet" href="css/bootstrap_3.7.css">
</head>

<body>

	<div class="row">
		<div class="col-md-6">
			<h3>Employee edit</h3>
		</div>
	</div>
	<%
		String isLogin = (String) session.getAttribute("username");
		if (isLogin != "admin") {
			response.sendRedirect("HolidayBookingAppServlet");
		}
		%>

	<form method="POST" action="EditEmployeeServlet">
		<input type="hidden" name="id" value="${employee.id}">
		<div class="form-group">
			<label>First_name</label> <input class="form-control"
				name="firstName" placeholder="First_name"
				value="${employee.firstName}" />
		</div>

		<div class="form-group">
			<label>Last_name</label> <input class="form-control" name="lastName"
				placeholder="Last_name" value="${employee.lastName}" />
		</div>
		<div class="form-group">
			<label>Email</label> <input class="form-control" name="email"
				placeholder="email" value="${employee.email}" />
		</div>
		<div class="form-group">
			<label>Phone_number</label> <input class="form-control"
				name="phoneNumber" placeholder="Phone_number"
				value="${employee.phoneNumber}" />
		</div>
		<div class="form-group">
			<label>Home_address</label> <input class="form-control"
				name="homeAddress" placeholder="Home_address"
				value="${employee.homeAddress}" />
		</div class="form-group">
		<div class="form-group">
			<label>Hire_date</label> <input class="form-control" name="hireDate"
				placeholder="Hire_date" value="${employee.hireDate}" />
		</div>
		<div class="form-group">
			<label>Salary</label> <input class="form-control" name="salary"
				placeholder="Salary" value="${employee.salary}" />
		</div>
		<div class="form-group">
			<label>Password</label> <input class="form-control" name="password"
				placeholder="password" value="${employee.password}" />
		</div>

		<div class="form-group">
			<label>Select a Department</label> <select class="select"
				name="selectedEmployeeDepartment">
				<option value="${employee.depId}" selected="selected">${employee.depName}</option>
				<%
							List<DepartmentDTO> departments = (List) request.getAttribute("departments");
							for (DepartmentDTO e : departments) {
						%>
				<option value="<%=e.getIdDep()%>"><%=e.getDepartmentName()%></option>
				<%
							}
						%>
			</select>
		</div>

		<div class="form-group">
			<label>Select a Role</label> <select class="select form-control"
				name="selectedEmployeeRole">
				<option class="form-control" value="${employee.empRoleId}"
					selected="selected">${employee.empRoleName}</option>
				<%
							List<EmployeeRoleDTO> employeeRoles = (List) request.getAttribute("employeeRoles");
							for (EmployeeRoleDTO e : employeeRoles) {
						%>
				<option value="<%=e.getId()%>"><%=e.getRoleName()%></option>
				<%
							}
						%>
			</select>
		</div>

		<button type="submit">Submit</button>
		<a href="employee.jsp">Back</a>
	</form>
</body>
</html>
