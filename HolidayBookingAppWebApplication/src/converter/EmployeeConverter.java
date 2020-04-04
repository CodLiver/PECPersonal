package converter;


import java.awt.Window;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.json.JsonObject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;

import org.json.JSONArray;
import org.json.JSONObject;

import ejbholidaybookingapp.BookingDTO;
import ejbholidaybookingapp.EmployeeDTO;
import ejbholidaybookingapp.HolidayBookingAppBeanRemote;
import ejbholidaybookingapp.HolidayCondCheckingBeanRemote;
import ejbholidaybookingapp.RequestDTO;


@Path("con")
@Stateless
/*@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })*/
public class EmployeeConverter {
	//http://java.boot.by/ocewsd6-guide/ch04.html
	
	
	@EJB
	private HolidayBookingAppBeanRemote holidayBookingAppBean;
	
	@EJB
	private HolidayCondCheckingBeanRemote holidayCondCheckingBean;
	
	
	private String fileReader(String name) {
		
		 ClassLoader cl = this.getClass().getClassLoader();
		 InputStream is = cl.getResourceAsStream(name);
		 BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		     
		 String s="";
		    try {
				 while(reader.ready()) {
				     s+= reader.readLine();
				}
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
		    
		return s;
	}
	
	@GET
	@Path("/user/{name}/requests")
	public Response getUserRequests(@PathParam("name") String name) {

		List<RequestDTO> reqEmp= holidayBookingAppBean.getAllRequestperEmp(name);
		String[] approved = {"Rejected", "Approved", "Pending"};
		String message;	
		try {
			
			JSONArray reqEmp_array = new JSONArray();
			
			
			for (RequestDTO r : reqEmp) {
				JSONArray reqEmp_in = new JSONArray();
				
				reqEmp_in.put(r.getBegin_date());
				reqEmp_in.put(r.getEnd_date());
				reqEmp_in.put(r.getDuration());
				reqEmp_in.put(r.getHoliday_remaining());
				reqEmp_in.put(r.getPeak_time());
				reqEmp_in.put(approved[r.getStatus()]);
				
				reqEmp_array.put(reqEmp_in);
				
			}
			
			JSONObject json = new JSONObject();
			json.put("data", reqEmp_array);
			
			/*JSONObject json = new JSONObject();
			json.put("name", "student");
	
			
			JSONObject item = new JSONObject();
			item.put("information", "test");
			item.put("id", 3);
			item.put("name", "course1");
			//array.add(item);
	
			json.put("course", array);*/
	
			message = json.toString();
			
		return Response.ok(message).build();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return Response.ok("502").build();
	    //return result;
	}
	
	
	@POST
	@Path("/post/request")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendRequest(InputStream incomingData) {//Response getFile() {
		//https://www.tutorialspoint.com/ejb/ejb_message_driven_beans.htm
		//https://www.javatpoint.com/message-driven-bean
		
		StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
			
			
			String jsonLoginStr = crunchifyBuilder.toString();
			System.out.println("Data Received: " + jsonLoginStr);
			
			JSONObject obj = new JSONObject(jsonLoginStr);
			
			String beginDate = (String) obj.get("beginDate");
			String endDate = (String) obj.get("endDate");
			String email = (String) obj.get("username");
			
			System.out.println("req");
			System.out.println(beginDate);
			System.out.println(endDate);
			
			//in case some holidays are over a year or negative.
			int inCheck=Integer.parseInt(endDate.substring(0, 4))-Integer.parseInt(beginDate.substring(0, 4));
			if(inCheck>1 || inCheck<0) {				
				JSONObject json = new JSONObject();
				json.put("error", "You cannot take unusual holidays.");
				String message = json.toString();
				return Response.ok(message).build();
			}
			
			EmployeeDTO thatEmployee = holidayBookingAppBean.getEmployeeByEmail(email);
			List<BookingDTO> listofAllDepBooking = holidayBookingAppBean.getAllBookingsByDep(thatEmployee.getDepId());//getAllBookingsperEmp(email);
			List<EmployeeDTO> allEmpListPerDep=holidayBookingAppBean.getAllEmployeesByDep(thatEmployee.getDepId());

			int remaining=holidayCondCheckingBean.holidayRemainingCalc(listofAllDepBooking, beginDate, endDate, thatEmployee);
			
			try {
				
				SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy");
				Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(beginDate);
				Date finDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
				
				int intDiffDays = holidayCondCheckingBean.dateDiffCalc(finDate,startDate);//(int) diffDays;
				
				if(intDiffDays<0) {
					JSONObject json = new JSONObject();
					json.put("error", "You cannot take holidays to the past.");
					String message = json.toString();
					return Response.ok(message).build();
				}
				
				int xMassDeductor=holidayCondCheckingBean.xMassCheck(beginDate,endDate);//depends on deducting duration too.
				
				if(xMassDeductor<11&&xMassDeductor>0) {
					JSONObject json = new JSONObject();
					json.put("error", "Please do not choose dates between 23December-03Jan. Ex: (start)20Dec-5Jan(end) request is OK, 25Dec-X or x-1Jan are Not OK. ");
					String message = json.toString();
					return Response.ok(message).build();
				}
		
		
				int absoluteDuration=intDiffDays-xMassDeductor;
				
				remaining-=absoluteDuration;
				
				if(remaining<0) {
					JSONObject json = new JSONObject();
					json.put("error", "You exceed your holiday allowance Please pick closer date.");
					String message = json.toString();
					return Response.ok(message).build();
				}
				
				if(absoluteDuration==0) {
					RequestDTO newReq = new RequestDTO(0, converter.format(startDate).toString(), converter.format(finDate).toString(), absoluteDuration, remaining, thatEmployee.getId(), 0, 2);
					holidayBookingAppBean.addNewRequest(newReq);
	
					JSONObject json = new JSONObject();
					json.put("error", "OK");
					String message = json.toString();
					return Response.ok(message).build();
				}
				
				
				////
				
				if(thatEmployee.getEmpRoleId()<2) {//managerial branch
					
					int[] condBreakerList= holidayCondCheckingBean.holAllowanceForHeadDep(startDate,finDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
					
					String errorMessage="";
					if(condBreakerList[1]==1)
						errorMessage+="head master and ";
					if (condBreakerList[3]==1)
						errorMessage+="60%-threshold employee,";
					
					if(errorMessage!="") {
						JSONObject json = new JSONObject();
						json.put("error", "You are the only available "+errorMessage+" you cannot leave your people!");
						String message = json.toString();
						return Response.ok(message).build();
					}
						
					
				}else if(thatEmployee.getEmpRoleId()==2 || thatEmployee.getEmpRoleId()==5) {//Senior branch
					
					int[] condBreakerList=holidayCondCheckingBean.holAllowanceForManSen(startDate,finDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
					
					String errorMessage="";
					if(condBreakerList[2]==1)
						errorMessage+="senior staff and ";
					if (condBreakerList[3]==1)
						errorMessage+="60%-threshold employee,";
					
					if(errorMessage!="") {
						JSONObject json = new JSONObject();
						json.put("error", "You are the only available "+errorMessage+" you cannot leave your people!");
						String message = json.toString();
						return Response.ok(message).build();
					}
					
				}else {//for regulars, check 60% rule
					
					int[] condBreakerList=holidayCondCheckingBean.holAllowanceForNonSpecs(startDate,finDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
					
					String errorMessage="";
					if (condBreakerList[3]==1)
						errorMessage+="60%-threshold employee,";
					
					if(errorMessage!="") {
						JSONObject json = new JSONObject();
						json.put("error", "You are the only available "+errorMessage+" you cannot leave your people!");
						String message = json.toString();
						return Response.ok(message).build();
					}
					
				}
				
				//System.out.println(intDiffDays);
				//change 34 to get holdayentit area.
		

				//if(!didHaveErrorMessage) { in case of any stupid duplications which I doubt due to returns 
					RequestDTO newReq = new RequestDTO(0, converter.format(startDate).toString(), converter.format(finDate).toString(), absoluteDuration, remaining, thatEmployee.getId(), 0, 2);
					holidayBookingAppBean.addNewRequest(newReq);
					
					JSONObject json = new JSONObject();
					json.put("error", "OK");
					String message = json.toString();
					return Response.ok(message).build();
				
				//}
				
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
			e.printStackTrace();
		}
		
		
		return Response.ok("502").build();
		
	}
	
	
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(InputStream incomingData) {//Response getFile() {
		//https://www.baeldung.com/httpurlconnection-post
		//https://stackoverflow.com/questions/1485708/how-do-i-do-a-http-get-in-java
		
		StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
			
			
			String jsonLoginStr = crunchifyBuilder.toString();
			System.out.println("Data Received: " + jsonLoginStr);
			
			JSONObject obj = new JSONObject(jsonLoginStr);
			
			String email = (String) obj.get("email");
			String password = (String) obj.get("password");
			
			System.out.println("epass");
			System.out.println(email);
			System.out.println(password);
			
			int result = holidayBookingAppBean.login(email,password);
			return Response.ok(result).build();
			
			
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
			e.printStackTrace();
		}
		
		
		return Response.ok("502").build();
		
		//JSOObject jsonObject = new JSObject(jsonLoginStr);
		//JsonObject obj = new JsonObject(jsonLoginStr);
		
		
		
		/*
		System.out.println(email);

		try {
			int result = 2;// holidayBookingAppBean.login(email,password);
			
			if(result==2) {			
				return Response.ok(email+password).build();
				
			}else {
				return Response.ok().entity(fileReader("login.html")).cookie(new NewCookie("cookieResponse", "cookieValueInRet")).build();
			}
				
		}catch(Exception e){return null;} finally {
			//out.close();
		}*/
	}	
	
}