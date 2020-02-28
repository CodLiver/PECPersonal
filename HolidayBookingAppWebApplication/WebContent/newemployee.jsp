<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="ejbholidaybookingapp.DepartmentDTO"%>
<%@ page import="ejbholidaybookingapp.EmployeeRoleDTO"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Add new Employee</title>
	</head>

	<body>
		<%
		String isLogin = (String) session.getAttribute("username");
		if (isLogin != "admin") {
			response.sendRedirect("HolidayBookingAppServlet");
		}
		%>
		<p>New employee</p>
		<form method="POST" action="NewEmployeeServlet">
			<div>
				<label>First_name</label> <input name="firstName"
					placeholder="First_name" />
			</div>

			<div>
				<label>Last_name</label> <input name="lastName"
					placeholder="Last_name" />
			</div>
			<div>
				<label>Email</label> <input name="email" placeholder="email" />
			</div>
			<div>
				<label>Phone_number</label> <input name="phoneNumber"
					placeholder="Phone_number" />
			</div>
			<div>
				<label>Home_address</label> <input name="homeAddress"
					placeholder="Home_address" />
			</div>
			<div>
				<label>Hire_date</label> <input name="hireDate"
					placeholder="Hire_date" />
			</div>
			<div>
				<label>Salary</label> <input name="salary" placeholder="Salary" />
			</div>
			<div>
				<label>Password</label> <input name="password" placeholder="password" />
			</div>

			<div>
				<label>Select a Department</label>
			    <select class="select" name="selectedEmployeeDepartment">
			        <option value="" selected="selected" disabled="disabled">Select an department:</option>
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

			<div>
				<label>Select a Role</label>
			    <select class="select" name="selectedEmployeeRole">
			        <option value="" selected="selected" disabled="disabled">Select a role:</option>
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
			<a href="EmployeesServlet">Back</a>
		</form>
	</body>
</html>
