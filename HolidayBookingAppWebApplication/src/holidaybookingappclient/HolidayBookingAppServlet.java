package holidaybookingappclient;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import ejbholidaybookingapp.CalculatorResultDTO;
import ejbholidaybookingapp.HolidayBookingAppBeanRemote;

/**
 * Servlet implementation class HolidayBookingAppServlet
 */
@WebServlet("/HolidayBookingAppServlet")
public class HolidayBookingAppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HolidayBookingAppServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@EJB
	private HolidayBookingAppBeanRemote holidayBookingAppBean;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			boolean result = holidayBookingAppBean.login(request.getParameter("email"),
					request.getParameter("password"));
			if (result) {
				HttpSession session = request.getSession(false);
				//save message in session
				session.setAttribute("username", "admin");
				response.sendRedirect("EmployeesServlet");
			} else {
				request.setAttribute("errorLoginMessage", "Invalid username, password or employee role! Please try again.");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
		} finally {
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}