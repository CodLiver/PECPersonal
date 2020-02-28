  
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
import ejbholidaybookingapp.EmployeeDTO;
import ejbholidaybookingapp.EmployeeRoleDTO;
import ejbholidaybookingapp.HolidayBookingAppBeanRemote;

/**
 * Servlet implementation class EditEmployeeServlet
 */
@WebServlet("/EditEmployeeServlet")
public class EditEmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EditEmployeeServlet() {
        super();
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int employeeId = Integer.parseInt(request.getParameter("id"));
		EmployeeDTO queryResult = holidayBookingAppBean.getEmployeeById(employeeId);
		if (queryResult != null) {
	    	request.setAttribute("employee", queryResult);

			List<DepartmentDTO> departments = holidayBookingAppBean.getDepartments();
			request.setAttribute("departments", departments);

			List<EmployeeRoleDTO> employeeRoles = holidayBookingAppBean.getRoles();
			request.setAttribute("employeeRoles", employeeRoles);

	    	request.getRequestDispatcher("/editemployee.jsp").forward(request, response);
		} else {
			// error
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		EmployeeDTO queryResult = holidayBookingAppBean.getEmployeeById(id);

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

		queryResult.setFirstName(firstName);
		queryResult.setLastName(lastName);
		queryResult.setEmail(email);
		queryResult.setPhoneNumber(phoneNumber);
		queryResult.setHomeAddress(homeAddress);
		queryResult.setHireDate(hireDate);
		queryResult.setSalary(salary);
		queryResult.setPassword(password);
		queryResult.setDepId(departmentId);
		queryResult.setEmpRoleId(employeeRoleId);

		holidayBookingAppBean.editEmployee(queryResult);
		response.sendRedirect("EmployeesServlet");
	}

}