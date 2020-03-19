package holidaybookingappclient;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

/**
 * Servlet implementation class NewEmployeeServlet
 */
@WebServlet("/NewRequestServlet")
public class NewRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public NewRequestServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*private Date parseDate(String dt) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(dt);
		} catch (Exception e) {
			return null;
		}
	}*/

	@EJB
	private HolidayBookingAppBeanRemote holidayBookingAppBean;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*List<DepartmentDTO> departments = holidayBookingAppBean.getDepartments();
		request.setAttribute("departments", departments);

		List<EmployeeRoleDTO> employeeRoles = holidayBookingAppBean.getRoles();
		request.setAttribute("employeeRoles", employeeRoles);*/
		
		request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//doGet(request, response);
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		//DURATION | HOL_DAYS_ENTITLEMENT | HOL_DAYS_REMAINING | ID_EMP | ID_PEAK_TIME | ID_STATUS
		//check if peak time
		HttpSession session = request.getSession();
		String email = session.getAttribute("email").toString();
		EmployeeDTO thatEmployee = holidayBookingAppBean.getEmployeeByEmail(email);
		
		List<BookingDTO> listofEmpBooking = holidayBookingAppBean.getAllBookingsperEmp(email);
		
		int remaining;
		if (!listofEmpBooking.isEmpty()) {
			
			BookingDTO lastBook = listofEmpBooking.get(listofEmpBooking.size() - 1);			
			
			if(lastBook.getBegin_date().substring(6, 10)!=beginDate.substring(0, 4)) {
				remaining=thatEmployee.getHoliday_entitlement();
			}else {
				remaining=lastBook.getHoliday_remaining();
			}
			
		}else {
			remaining=thatEmployee.getHoliday_entitlement();
		}
		
		//beginDate.substring(0, 4); use this as const check of that year area.
		
		try {
			SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy");
			
			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(beginDate);
			long startTime = startDate.getTime();
			
			Date finDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
			long endTime = finDate.getTime();  
			
			long diffTime = endTime - startTime;
			long diffDays = diffTime / (1000 * 60 * 60 * 24);
			int intDiffDays = (int) diffDays;
			
			System.out.println(intDiffDays);
			//change 34 to get holdayentit area.
			remaining-=intDiffDays;
			int pendRej=2;
			if(remaining<0) {
				//pendRej=0;
				request.setAttribute("errorLoginMessage", "You exceed your holiday allowance Please pick closer date.");
				request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
			}else {
				RequestDTO newReq = new RequestDTO(0, converter.format(startDate).toString(), converter.format(finDate).toString(), intDiffDays, remaining, thatEmployee.getId(), 0, pendRej);
				holidayBookingAppBean.addNewRequest(newReq);
				response.sendRedirect("EmployeesLoginServlet");
			}
			

			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}

}