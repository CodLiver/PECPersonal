package holidaybookingappclient;

import java.lang.Math; 

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

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
	
	@EJB
	private HolidayCondCheckingBeanRemote holidayCondCheckingBean;

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
		
		boolean didHaveErrorMessage=false;
		
		
		//in case some holidays are over a year or negative.
		int inCheck=Integer.parseInt(endDate.substring(0, 4))-Integer.parseInt(beginDate.substring(0, 4));
		if(inCheck>1 || inCheck<0) {
			didHaveErrorMessage=true;
			request.setAttribute("errorLoginMessage", "You cannot take unusual holidays.");
			request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
		}

		EmployeeDTO thatEmployee = holidayBookingAppBean.getEmployeeByEmail(email);
		List<BookingDTO> listofAllDepBooking = holidayBookingAppBean.getAllBookingsByDep(thatEmployee.getDepId());//getAllBookingsperEmp(email);
		List<EmployeeDTO> allEmpListPerDep=holidayBookingAppBean.getAllEmployeesByDep(thatEmployee.getDepId());
		
		
		//api this too
		int remaining=holidayCondCheckingBean.holidayRemainingCalc(listofAllDepBooking, beginDate, endDate, thatEmployee);
		int holRem=remaining;
		
		//beginDate.substring(0, 4); use this as const check of that year area.
		
		try {
			
			//do xmass here
			SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy");
			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(beginDate);
			Date finDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);			
			
			int intDiffDays = holidayCondCheckingBean.dateDiffCalc(finDate,startDate);//(int) diffDays;
			
			if(intDiffDays<0) {
				didHaveErrorMessage=true;
				request.setAttribute("errorLoginMessage", "You cant holiday to past.");
				request.setAttribute("errorLoginMessage2", "Try "+endDate+" to "+beginDate+" if allowed.");
				request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
			}
			
			int xMassDeductor=holidayCondCheckingBean.xMassCheck(beginDate,endDate);//depends on deducting duration too.
			
			System.out.println("reee xMassDeductor");
			System.out.println(xMassDeductor);
			if(xMassDeductor<11&&xMassDeductor>0) {
				didHaveErrorMessage=true;
				request.setAttribute("errorLoginMessage", "Please do not choose dates between 23December-03Jan. Ex: (start)20Dec-5Jan(end) request is OK, 25Dec-X or x-1Jan are Not OK. ");
				request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
			}
	
			int absoluteDuration=intDiffDays-xMassDeductor;
			
			
			/*System.out.println("holiday");
			System.out.println(intDiffDays);
			System.out.println(xMassDeductor);
			System.out.println(absoluteDuration);*/
			
			remaining-=absoluteDuration;
			if(remaining<0) {
				didHaveErrorMessage=true;
				request.setAttribute("errorLoginMessage", "You exceed your holiday allowance Please pick closer date.");
				request.setAttribute("errorLoginMessage", "Your holiday allowings are "+String.valueOf(holRem)+" days.");
				request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
			}
			
			if(absoluteDuration==0) {
				didHaveErrorMessage=true;
				RequestDTO newReq = new RequestDTO(0, converter.format(startDate).toString(), converter.format(finDate).toString(), absoluteDuration, remaining, thatEmployee.getId(), 0, 2);
				holidayBookingAppBean.addNewRequest(newReq);
				response.sendRedirect("EmployeesLoginServlet");
			}
			
			
			if(thatEmployee.getEmpRoleId()<2) {//managerial branch
				
				int[] condBreakerList= holidayCondCheckingBean.holAllowanceForHeadDep(startDate,finDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
				
				String errorMessage="";
				if(condBreakerList[1]==1)
					errorMessage+="head master and ";
				if (condBreakerList[3]==1)
					errorMessage+="60%-threshold employee,";
				
				if(errorMessage!="") {
					didHaveErrorMessage=true;
					request.setAttribute("errorLoginMessage", "You are the only available "+errorMessage+" you cannot leave your people!");
					
					boolean didSecondErr=true;
					//absoluteDuration
					String aNewDate="Nan";
					String aNewFinDate = "Nan";
					int limit=0;
					while(didSecondErr && limit<5) {
						limit++;
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
						cal.setTime(startDate);
						String startyear=String.valueOf(cal.get(Calendar.YEAR));
						String startMonth = "0"+String.valueOf(new Random().nextInt(9)+1);
						String startDay = String.valueOf(new Random().nextInt(19)+10);
						aNewDate = startyear+"-"+startMonth+"-"+startDay;
						Date startNewDate = new SimpleDateFormat("yyyy-MM-dd").parse(aNewDate);
						
						cal.setTime(startNewDate);
						cal.add(Calendar.DATE, absoluteDuration);
						Date finNewDate = cal.getTime();
						String finyear=String.valueOf(cal.get(Calendar.YEAR));
						String finmonth=String.valueOf(cal.get(Calendar.MONTH)+1);
						String finday=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
						aNewFinDate = finyear+"-"+finmonth+"-"+finday;
						
						int[] condBreakerList2= holidayCondCheckingBean.holAllowanceForHeadDep(startNewDate,finNewDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
						
						boolean didThirdErr=false;
						for(int i : condBreakerList2) {
							if(i==1) 
								didThirdErr=true;
						}
						didSecondErr=didThirdErr;
					}
					if (limit<5) {
						request.setAttribute("errorLoginMessage2", "You can choose this holiday: "+aNewDate+" to "+aNewFinDate);
					}else {
						request.setAttribute("errorLoginMessage2", "and you might probably be the only person, so you cannot take holiday ever.");
					}
					
					request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
				}
					
				
			}else if(thatEmployee.getEmpRoleId()==2 || thatEmployee.getEmpRoleId()==5) {//Senior branch
				
				int[] condBreakerList=holidayCondCheckingBean.holAllowanceForManSen(startDate,finDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
				
				
				System.out.println("manser-break");
				System.out.println(condBreakerList[2]);
				
				String errorMessage="";
				if(condBreakerList[2]==1)
					errorMessage+="senior staff and ";
				if (condBreakerList[3]==1)
					errorMessage+="60%-threshold employee,";
				
				if(errorMessage!="") {
					didHaveErrorMessage=true;
					request.setAttribute("errorLoginMessage", "You are the only available "+errorMessage+" you cannot leave your people!");
					
					
					boolean didSecondErr=true;
					//absoluteDuration
					String aNewDate="Nan";
					String aNewFinDate = "Nan";
					int limit=0;
					while(didSecondErr && limit<5) {
						limit++;
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
						cal.setTime(startDate);
						String startyear=String.valueOf(cal.get(Calendar.YEAR));
						String startMonth = "0"+String.valueOf(new Random().nextInt(9)+1);
						String startDay = String.valueOf(new Random().nextInt(19)+10);
						aNewDate = startyear+"-"+startMonth+"-"+startDay;
						Date startNewDate = new SimpleDateFormat("yyyy-MM-dd").parse(aNewDate);
						
						cal.setTime(startNewDate);
						cal.add(Calendar.DATE, absoluteDuration);
						Date finNewDate = cal.getTime();
						String finyear=String.valueOf(cal.get(Calendar.YEAR));
						String finmonth=String.valueOf(cal.get(Calendar.MONTH)+1);
						String finday=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
						aNewFinDate = finyear+"-"+finmonth+"-"+finday;
						
						int[] condBreakerList2= holidayCondCheckingBean.holAllowanceForManSen(startNewDate,finNewDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
						
						boolean didThirdErr=false;
						for(int i : condBreakerList2) {
							if(i==1) 
								didThirdErr=true;
						}
						didSecondErr=didThirdErr;
					}
					if (limit<5){
						request.setAttribute("errorLoginMessage2", "You can choose this holiday: "+aNewDate+" to "+aNewFinDate);
					}else {
						request.setAttribute("errorLoginMessage2", "and you might probably be the only person, so you cannot take holiday ever.");
					}
					
					request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
				}
				
			}else {//for regulars, check 60% rule
				
				int[] condBreakerList=holidayCondCheckingBean.holAllowanceForNonSpecs(startDate,finDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
				
				String errorMessage="";
				if (condBreakerList[3]==1)
					errorMessage+="60%-threshold employee,";
				
				if(errorMessage!="") {
					didHaveErrorMessage=true;
					request.setAttribute("errorLoginMessage", "You are the only available "+errorMessage+" you cannot leave your people!");
					
					
					
					boolean didSecondErr=true;
					//absoluteDuration
					String aNewDate="Nan";
					String aNewFinDate = "Nan";
					int limit=0;
					while(didSecondErr && limit<5) {
						limit++;
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
						cal.setTime(startDate);
						String startyear=String.valueOf(cal.get(Calendar.YEAR));
						String startMonth = "0"+String.valueOf(new Random().nextInt(9)+1);
						String startDay = String.valueOf(new Random().nextInt(19)+10);
						aNewDate = startyear+"-"+startMonth+"-"+startDay;
						Date startNewDate = new SimpleDateFormat("yyyy-MM-dd").parse(aNewDate);
						
						cal.setTime(startNewDate);
						cal.add(Calendar.DATE, absoluteDuration);
						Date finNewDate = cal.getTime();
						String finyear=String.valueOf(cal.get(Calendar.YEAR));
						String finmonth=String.valueOf(cal.get(Calendar.MONTH)+1);
						String finday=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
						aNewFinDate = finyear+"-"+finmonth+"-"+finday;
						
						int[] condBreakerList2= holidayCondCheckingBean.holAllowanceForNonSpecs(startNewDate,finNewDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
						
						boolean didThirdErr=false;
						for(int i : condBreakerList2) {
							if(i==1) 
								didThirdErr=true;
						}
						didSecondErr=didThirdErr;
					}
					
					if (limit<5){
						request.setAttribute("errorLoginMessage2", "You can choose this holiday: "+aNewDate+" to "+aNewFinDate);
					}else {
						request.setAttribute("errorLoginMessage2", "and you might probably be the only person, so you cannot take holiday ever.");
					}
					
					request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
				}
				
			}
			
			//System.out.println(intDiffDays);
			//change 34 to get holdayentit area.
	

			if(!didHaveErrorMessage) {
				RequestDTO newReq = new RequestDTO(0, converter.format(startDate).toString(), converter.format(finDate).toString(), absoluteDuration, remaining, thatEmployee.getId(), 0, 2);
				holidayBookingAppBean.addNewRequest(newReq);
				//check if sent by head or not.
				response.sendRedirect("EmployeesLoginServlet");
			
			}

			

			
		} catch (ParseException e) {
			e.printStackTrace();
		}  

	}

}