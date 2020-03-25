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
//import ejbholidaybookingapp.HolidayCondCheckingBeanRemote;

@WebServlet("/AcceptRejectRequestServlet")
public class AcceptRejectRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
    private HolidayBookingAppBeanRemote holidayBookingAppBean;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int requestId = Integer.parseInt(request.getParameter("id"));
		int accden = Integer.parseInt(request.getParameter("decision"));
		
		System.out.println("req area");
		System.out.println(requestId);
		System.out.println(accden);
		
		RequestDTO req = holidayBookingAppBean.getRequestById(requestId);
		
		System.out.println(req.getBegin_date());
		
		if (req != null) {
			
			if (accden==1) {
				System.out.println(req.getEnd_date());
				holidayBookingAppBean.addRequestToBooking(req);
			}else {
				//holidayBookingAppBean.deleteRequest(req);
				holidayBookingAppBean.rejectRequest(req);
			}
			
			System.out.println("resp area");
			response.sendRedirect("RequestsAndBookingsServlet");
		} else {
			// error
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}
