package holidaybookingappclient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejbholidaybookingapp.DepartmentDTO;
import ejbholidaybookingapp.EmployeeRoleDTO;
import ejbholidaybookingapp.EmployeeDTO;
import ejbholidaybookingapp.HolidayBookingAppBeanRemote;

/**
 * Servlet implementation class NewEmployeeServlet
 */
@WebServlet("/NewEmployeeServlet")
public class NewEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public NewEmployeeServlet() {
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
		List<DepartmentDTO> departments = holidayBookingAppBean.getDepartments();
		request.setAttribute("departments", departments);

		List<EmployeeRoleDTO> employeeRoles = holidayBookingAppBean.getRoles();
		request.setAttribute("employeeRoles", employeeRoles);
		
		request.getRequestDispatcher("/newemployee.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//doGet(request, response);
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String phoneNumber = request.getParameter("phoneNumber");
		String homeAddress = request.getParameter("homeAddress");
		String hireDate = request.getParameter("hireDate");
		Integer salary = Integer.parseInt(request.getParameter("salary"));
		String password = request.getParameter("password");
		
		int departmentId = Integer.parseInt(request.getParameter("selectedEmployeeDepartment"));
		int employeeRoleId = Integer.parseInt(request.getParameter("selectedEmployeeRole"));

		EmployeeDTO newEmp = new EmployeeDTO(0, employeeRoleId, null, departmentId, null, email, password, firstName, lastName,
				phoneNumber, hireDate, salary, homeAddress);

		holidayBookingAppBean.addNewEmployee(newEmp);
		response.sendRedirect("EmployeesServlet");
	}

}