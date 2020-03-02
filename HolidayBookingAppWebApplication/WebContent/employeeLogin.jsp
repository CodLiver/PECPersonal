
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="ejbholidaybookingapp.RequestDTO"%>
<!DOCTYPE html>
<html>
<body>
	<div>
	<%
		String isLogin = (String) session.getAttribute("username");
		if (isLogin != "employee") {
			response.sendRedirect("HolidayBookingAppServlet");
			//response.sendRedirect("/login.jsp");
		}
	%>
		<div>
			<h3>Requests</h3>
		</div>
		<!-- 		<form method="POST" action="NewEmployeeServlet">
			<input type="submit" value="New employee" />
		</form> -->

		<div>
			<a href="NewRequestServlet"> Submit new Request</a>
		</div>
	</div>
	<table>
		<tr>
			<th>Begin Date</th>
			<th>End Date</th>
			<th>Duration</th>
			<th>Total Holiday Given</th>
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
			<td><%=r.getHoliday_entitlement()%></td>
			<td><%=r.getHoliday_remaining()%></td>
			<td><%=r.getPeak_time()%></td>
			<td><%=approved[r.getStatus()]%></td>
			<td></td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>