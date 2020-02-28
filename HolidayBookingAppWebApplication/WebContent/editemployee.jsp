<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="ejbholidaybookingapp.EmployeeDTO"%>
<%@ page import="ejbholidaybookingapp.EmployeeRoleDTO"%>
<%@ page import="ejbholidaybookingapp.DepartmentDTO"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Edit Employee</title>
	</head>

	<body>
		<%
		String isLogin = (String) session.getAttribute("username");
		if (isLogin != "admin") {
			response.sendRedirect("HolidayBookingAppServlet");
		}
		%>
		<p>Edit employee</p>
		
		<form method="POST" action="EditEmployeeServlet">
			<input type="hidden" name="id" value="${employee.id}">
			<div>
				<label>First_name</label> <input name="firstName"
					placeholder="First_name" value="${employee.firstName}" />
			</div>

			<div>
				<label>Last_name</label> <input name="lastName"
					placeholder="Last_name" value="${employee.lastName}" />
			</div>
			<div>
				<label>Email</label> <input name="email" placeholder="email" value="${employee.email}" />
			</div>
			<div>
				<label>Phone_number</label> <input name="phoneNumber"
					placeholder="Phone_number" value="${employee.phoneNumber}" />
			</div>
			<div>
				<label>Home_address</label> <input name="homeAddress"
					placeholder="Home_address" value="${employee.homeAddress}" />
			</div>
			<div>
				<label>Hire_date</label> <input name="hireDate"
					placeholder="Hire_date" value="${employee.hireDate}" />
			</div>
			<div>
				<label>Salary</label> <input name="salary" placeholder="Salary" value="${employee.salary}" />
			</div>
			<div>
				<label>Password</label> <input name="password" placeholder="password" value="${employee.password}" />
			</div>

			<div>
				<label>Select a Department</label>
			    <select class="select" name="selectedEmployeeDepartment">
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

			<div>
				<label>Select a Role</label>
			    <select class="select" name="selectedEmployeeRole">
			        <option value="${employee.empRoleId}" selected="selected">${employee.empRoleName}</option>
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