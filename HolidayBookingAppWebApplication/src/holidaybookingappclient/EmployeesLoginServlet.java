package holidaybookingappclient;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ejbholidaybookingapp.EmployeeDTO;
import ejbholidaybookingapp.RequestDTO;
import ejbholidaybookingapp.HolidayBookingAppBeanRemote;

/**
 * Servlet implementation class Employees
 */
@WebServlet("/EmployeesLoginServlet")
public class EmployeesLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesLoginServlet() {
        super();
    }

    @EJB
	private HolidayBookingAppBeanRemote holidayBookingAppBean;
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		List<RequestDTO> getAllRequestperEmp = holidayBookingAppBean.getAllRequestperEmp(session.getAttribute("email").toString());
		request.setAttribute("getAllRequestperEmp", getAllRequestperEmp);
		request.getRequestDispatcher("/employeeLogin.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}