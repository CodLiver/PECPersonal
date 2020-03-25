package holidaybookingappclient;

import java.lang.Math; 

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ejbholidaybookingapp.DepartmentDTO;
import ejbholidaybookingapp.EmployeeRoleDTO;
import ejbholidaybookingapp.EmployeeDTO;
import ejbholidaybookingapp.RequestDTO;
import ejbholidaybookingapp.BookingDTO;


import ejbholidaybookingapp.HolidayBookingAppBeanRemote;
import ejbholidaybookingapp.HolidayCondCheckingBeanRemote;

@WebServlet("/RequestsAndBookingsServlet")
public class RequestsAndBookingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public RequestsAndBookingsServlet() {
		super();
	}
	
	@EJB
	private HolidayBookingAppBeanRemote holidayBookingAppBean;
	
	@EJB
	private HolidayCondCheckingBeanRemote holidayCondCheckingBean;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		EmployeeDTO thatEmployee = holidayBookingAppBean.getEmployeeByEmail(session.getAttribute("email").toString());
		
		System.out.println("employeeObtained");
		
		List<Object> quitePythonicIKR = holidayCondCheckingBean.getAllOutstandingRequestsByDep(thatEmployee.getDepId());
		
		System.out.println("pythonic area");
		
		request.setAttribute("ruleBreakers",quitePythonicIKR.get(0));
		
		System.out.println("0 got");
		request.setAttribute("ruleBreakersRuleList", quitePythonicIKR.get(1));
		System.out.println("1 got");
		request.setAttribute("notRuleBreakers", quitePythonicIKR.get(2));
		System.out.println("2 got");
		//List<RequestDTO> getAllRequestperEmp = holidayBookingAppBean.getAllRequestperEmp(session.getAttribute("email").toString());
		//List<EmployeeDTO> allEmployees = holidayBookingAppBean.getAllEmployees();
		
		//request.setAttribute("ruleBreakers",allOutstandingRequests.get(0));
		//request.setAttribute("notRuleBreakers", allOutstandingRequests.get(1));
		
		List<BookingDTO> bookingsByDep = holidayBookingAppBean.getAllBookingsByDep(thatEmployee.getDepId());
		
		System.out.println("book by dep");
		
		request.setAttribute("bookings", bookingsByDep);
		
		request.setAttribute("departmentName", thatEmployee.getDepName());
		
		
		System.out.println("workemps");
		System.out.println(request.getParameter("workingEmps"));
		
		/*if(request.getParameter("workingEmps")==null) {
			request.setAttribute("workingEmps",null);
			request.setAttribute("holidayEmps",null);
		}*/
		
		request.getRequestDispatcher("/bookingsAndOutstandingRequests.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		
		List<List<EmployeeDTO>> filteredEmps = holidayCondCheckingBean.workingFilters(session.getAttribute("email").toString(),beginDate,endDate);
		
		
		request.setAttribute("workingEmps",filteredEmps.get(0));
		request.setAttribute("holidayEmps",filteredEmps.get(1));
		doGet(request, response);
	}
}
