package converter;

import java.awt.Window;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.JsonObject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;

import org.json.JSONArray;
import org.json.JSONObject;

//import build.classes.ejbholidaybookingapp.HolidayBookingAppBeanRemote;


@RequestScoped
@Path("con")
/*@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })*/
public class EmployeeConverter {
	
	//@EJB
	//private HolidayBookingAppBeanRemote holidayBookingAppBean;
	
	
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

	/*@GET
	@Path("/hi")
	public String convertCurrency() {
	    String result;
	    result = "<result>";
	    result += "hi";
	    result += "</result>";
	    return result;
	}*/
	
	@GET
	@Path("/hi")
	public String convertCurrency() {
	    /*String result;
	    result = "<result>";
	    result += "hi";
	    result += "</result>";*/
		
		String result="{\r\n" + 
				"  \"RestResponse\" : {\r\n" + 
				"    \"messages\" : [ \"More webservices are available at http://www.groupkt.com/post/f2129b88/services.htm\", \r\n" + 
				"    \"Country found matching code [IN].\" ],\r\n" + 
				"    \"result\" : {\r\n" + 
				"      \"name\" : \"India\",\r\n" + 
				"      \"alpha2_code\" : \"IN\",\r\n" + 
				"      \"alpha3_code\" : \"IND\"\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}";
	    return result;
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

			
			//int result = holidayBookingAppBean.login("email","password");
			return Response.ok(email+password).build();
			
			
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
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
	
	
	
	
	
	@GET
	@Path("/get")
	//@Produces(MediaType.APPLICATION_OCTET_STREAM)
	//@Produces(MediaType.TEXT_PLAIN)
	@Produces("text/html")
	public Response getLogin() {//Response getFile() {
		
			String s = fileReader("login.html");
		    
		    return Response.ok().entity(s).build();
		    //http://localhost:8180/HolidayAppRest/rest/con/get
	}
	
	int tek=0;
	@GET
	@Path("/EmployeeLogin")
	@Produces("text/html")
	public Response getEmpLogin(@CookieParam("cookieName") Cookie cookie) {//Response getFile() {
		
		if (tek==0) {
			tek++;
			//return Response.temporaryRedirect(new URI "con/EmployeeLogin").cookie(cookie).build();
	        URI location;
			try {
				location = new URI("con/EmployeeLogin");
				throw new WebApplicationException(Response.temporaryRedirect(location).build());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
			
		}
		
		System.out.println(cookie);
			String s = fileReader("employeeLogin.html");
		    
		    return Response.ok(s).cookie(new NewCookie(cookie)).build();
		    //http://localhost:8180/HolidayAppRest/rest/con/get
	}
	
	/*@GET
	@Path("/EmployeeLogin")
	@Produces("text/html")
	public Response getEmpLogin() {//Response getFile() {
			
		//System.out.println(cookie);
		System.out.println("heys");
			String s = fileReader("employeeLogin.html");
		    
		    return Response.ok(s).build();
		    //http://localhost:8180/HolidayAppRest/rest/con/get
	}*/
	
	@Context
    private UriInfo uriInfo;
	
	/*@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadInspectionFile(@FormDataParam("file") InputStream inputStream,
	        @FormDataParam("file") FormDataContentDisposition content,
	        @FormDataParam("path") String path)*/
	@POST
	@Path("/postLogin")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces("text/html")//@Produces(MediaType.TEXT_PLAIN)
	public Response loginCreds(@Context Request request,@FormParam("email") String email,
            @FormParam("password") String password) {//Response getFile() {
		
		/*System.out.println(email);
		//HolidayAppRest/rest/con/
		return Response.ok().entity("true").build();*/
		
		//@CookieParam("cookieName") Cookie cookie;
		//PrintWriter out = response.getWriter();
		try {
			int result = 2;// holidayBookingAppBean.login(email,password);
			
			if(result==2) {
				//URI uri = new URI("con/EmployeeLogin");
				NewCookie cookie = new NewCookie("name", "123");
				/*URI uri = uriInfo.getBaseUriBuilder().path("con/EmployeeLogin").build();
				
				return Response.temporaryRedirect(uri).cookie(cookie).build();*/
				
				return getEmpLogin(cookie);
				
				//return Response.entity()//.cookie(cookie).build();
				
				
				
				
				//
				//HttpSession session = request.//.getSession(false);
				 //URI location = new java.net.URI("con/EmployeeLogin");
				//return Response.ok().link(uri, "hey").build();
				//return Response.created(uri).build();
				//return Response.ok().entity(getEmpLogin()).cookie(new NewCookie("cookieResponse", "cookieValueInReturn")).build();
				//Response.ok().
			}else {
				return Response.ok().entity(fileReader("login.html")).cookie(new NewCookie("cookieResponse", "cookieValueInRet")).build();
			}
				
			/*if (result==1) {
				HttpSession session = request.getSession(false);
				//save message in session
				session.setAttribute("username", "admin");
				session.setAttribute("email", request.getParameter("email"));//maybe more?
				response.sendRedirect("EmployeesServlet");
			} else if(result==2) {
				HttpSession session = request.getSession(false);
				session.setAttribute("username", "employee");
				session.setAttribute("email", request.getParameter("email"));//maybe more?
				response.sendRedirect("EmployeesLoginServlet");	
			} else {
				request.setAttribute("errorLoginMessage", "Invalid username, password or employee role! Please try again.");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}*/
		}catch(Exception e){return null;} finally {
			//out.close();
		}
	}	
	
}
