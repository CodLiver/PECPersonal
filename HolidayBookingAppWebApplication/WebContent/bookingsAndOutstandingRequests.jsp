<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ page import="ejbholidaybookingapp.EmployeeDTO"%>
<%@ page import="ejbholidaybookingapp.RequestDTO"%>
<%@ page import="ejbholidaybookingapp.BookingDTO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script>
$(function() {
	  $("#filterer").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    $("#bookingsTable > tbody > tr").filter(function() { 
	    	$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
	    });
	  });
	});
</script>
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
			<a href="EmployeesServlet"><button class="btn btn-warning">
					Back</button></a>
		</div>

	</div>
	
	<h1><%=request.getAttribute("departmentName")%> Department Bookings and Requests</h1>
	<div class="row">
		<div class="col-md-6">
			<h3>Outstanding Holidays Requests</h3>
			
			<h5>Rule Breaking Holidays Requests</h5>
		</div>
	</div>
	
	<table class="table table-bordered table-striper table-hover">
		<tr>
			<th>Begin Date</th>
			<th>End Date</th>
			<th>Duration</th>
			<th>Remaining Holiday duration</th>
			<th>Peak time?</th>
			<th>ID_EMP</th>
			
			<th>Hol Remaining</th>
			<th>(Dep) Head here</th>
			<th>Exp. Staff here</th>
			<th>60% Staff</th>
		</tr>
		<%
			//String[] approved = {"Rejected", "Approved", "Pending"};
			List<RequestDTO> getAllRuleBreakingRequestByDep = (List) request.getAttribute("ruleBreakers");
			List<int[]> brokenRulesList = (List) request.getAttribute("ruleBreakersRuleList");
			
			for (int index=0; index<getAllRuleBreakingRequestByDep.size(); index++){
				
				RequestDTO r = getAllRuleBreakingRequestByDep.get(index);
		%>
		<tr>
			<td><%=r.getBegin_date()%></td>
			<td><%=r.getEnd_date()%></td>
			<td><%=r.getDuration()%></td>
			<td><%=r.getHoliday_remaining()%></td>
			<td><%=r.getPeak_time()%></td>
			<td><%=r.getId_emp()%></td>
			
			<td><%=brokenRulesList.get(index)[0]%></td>
			<td><%=brokenRulesList.get(index)[1]%></td>
			<td><%=brokenRulesList.get(index)[2]%></td>
			<td><%=brokenRulesList.get(index)[3]%></td>
						
			<td><a href="AcceptRejectRequestServlet?id=<%=r.getId()%>&decision=1"><button
						class="btn btn-warning">Accept</button></a></td>
			<td></td>
			<td><a href="AcceptRejectRequestServlet?id=<%=r.getId()%>&decision=0"><button
						class="btn btn-warning">Reject</button></a></td>
		</tr>
		<%
			}
		%>
	</table>
	
	
	<div class="row">
		<div class="col-md-6">
			<h5>Not Rule Breaking Holidays Requests</h5>
		</div>
	</div>
	
	<table class="table table-bordered table-striper table-hover">
		<tr>
			<th>Begin Date</th>
			<th>End Date</th>
			<th>Duration</th>
			<th>Remaining Holiday duration</th>
			<th>Peak time?</th>
			<th>Emp ID</th>
			<th>Accept</th>
			<th>Reject</th>
		</tr>
		<%
			//String[] approved = {"Rejected", "Approved", "Pending"};
			List<RequestDTO> getAllNotRuleBreakingRequestByDep = (List) request.getAttribute("notRuleBreakers");
				
			for (int index=0; index<getAllNotRuleBreakingRequestByDep.size(); index++){
				
				RequestDTO r = getAllNotRuleBreakingRequestByDep.get(index);
		%>
		<tr>
			<td><%=r.getBegin_date()%></td>
			<td><%=r.getEnd_date()%></td>
			<td><%=r.getDuration()%></td>
			<td><%=r.getHoliday_remaining()%></td>
			<td><%=r.getPeak_time()%></td>
			<td><%=r.getId_emp()%></td>
						
			<td><a href="AcceptRejectRequestServlet?id=<%=r.getId()%>&decision=1"><button
						class="btn btn-warning">Accept</button></a></td>
			<td></td>
			<td><a href="AcceptRejectRequestServlet?id=<%=r.getId()%>&decision=0"><button
						class="btn btn-warning">Reject</button></a></td>
		</tr>
		<%
			}
		%>
	</table>
	
	<div class="row">
		<div class="col-md-6">
			<h3>Holiday Bookings</h3>
		</div>
		<input id="filterer" type="text" placeholder="Search..">
	</div>
	
	<table id="bookingsTable" class="table table-bordered table-striper table-hover">
		<thead>
		<tr>
			<th>Begin Date</th>
			<th>End Date</th>
			<th>Duration</th>
			<th>Rem. Holi. Dur.</th>
			<th>ID Employee</th>
		</tr>
		</thead>
		<tbody>
		<%
			List<BookingDTO> getBookingsByDep = (List) request.getAttribute("bookings");
				
			for (BookingDTO b : getBookingsByDep){
				
		%>
		<tr>
			<td><%=b.getBegin_date()%></td>
			<td><%=b.getEnd_date()%></td>
			<td><%=b.getDuration()%></td>
			<td><%=b.getHoliday_remaining()%></td>
			<td><%=b.getId_emp()%></td>						
		</tr>
		<%
			}
		%>
		</tbody>
	</table>

	<div class="row">
		<div class="col-md-6">
			<h3>Employee filter for the Day</h3>
		</div>
		
		<form method="POST" action="RequestsAndBookingsServlet">
		<div>
		<label>Begin Date</label> <input type="date" value="2020-03-01" min="2020-03-01" name="beginDate" />
		</div>

		<div>
			<label>End Date</label> <input type="date" value="2020-03-02" min="2020-03-02" name="endDate"/>
		</div>
			<button type="submit">View</button>
		</form>
	</div>
	
	<table class="table table-bordered table-striper table-hover">
		<tr>
			<th>ID</th>
			<th>First_name</th>
			<th>Last_name</th>
			<th>Email</th>
			<th>Department</th>
			<th>Total Holiday</th>
			<th>Is on Work?</th>
		</tr>
		<%
		System.out.println("workemps2");
		System.out.println(request.getAttribute("workingEmps"));	
		if(request.getAttribute("workingEmps")!=null){
					
			List<EmployeeDTO> workingEmps = (List) request.getAttribute("workingEmps");
			
			System.out.println("workemps3");
			System.out.println(workingEmps.isEmpty());
			System.out.println(workingEmps.size());
			System.out.println(workingEmps.get(0));
			System.out.println(workingEmps.get(0).getId());
			
			
			if(!workingEmps.isEmpty()){
				
				System.out.println("foremp");
			for (EmployeeDTO e : workingEmps) {
		%>
		<tr>
			<td><%=e.getId()%></td>
			<td><%=e.getFirstName()%></td>
			<td><%=e.getLastName()%></td>
			<td><%=e.getEmail()%></td>
			<td><%=e.getDepName()%></td>
			<td><%=e.getHoliday_entitlement()%></td>
			<td>Yes</td>
		</tr>
		<%
				}
			}
			List<EmployeeDTO> holidayEmps = (List) request.getAttribute("holidayEmps");
			if(!holidayEmps.isEmpty()){
			for (EmployeeDTO e : holidayEmps) {
		%>
		<tr>
			<td><%=e.getId()%></td>
			<td><%=e.getFirstName()%></td>
			<td><%=e.getLastName()%></td>
			<td><%=e.getEmail()%></td>
			<td><%=e.getDepName()%></td>
			<td><%=e.getHoliday_entitlement()%></td>
			<td>No</td>
		</tr>
		<%
				}
			}
		}
		%>
	</table>

	
	

</body>
</html>