package ejbholidaybookingapp;

import java.util.List;
import javax.ejb.Remote;

@Remote
public interface HolidayBookingAppBeanRemote {
	int login(String email, String password);
	RequestDTO getRequestById(int req_id);
	List<RequestDTO> getAllRequestperEmp(String email);
	List<RequestDTO> getAllRequestByDep(int id_dep);
	EmployeeDTO getEmployeeByEmail(String email);
	EmployeeDTO getEmployeeById(int id);
	List<BookingDTO> getAllBookings();
	List<BookingDTO> getAllBookingsperEmp(String email);
	List<BookingDTO> getAllBookingsByDep(int id_dep);
	List<EmployeeDTO> getAllEmployees();
	List<EmployeeDTO> getAllEmployeesByDep(int id_dep);
	List<DepartmentDTO> getDepartments();
	List<EmployeeRoleDTO> getRoles();
	boolean addRequestToBooking(RequestDTO requestBooked);
	boolean addNewEmployee(EmployeeDTO newEmp);
	boolean editEmployee(EmployeeDTO updateEmp);
	boolean deleteEmployee(EmployeeDTO deleteEmp);
	boolean deleteRequest(RequestDTO deleteReq);
	boolean rejectRequest(RequestDTO rejectReq);
	boolean addNewRequest(RequestDTO newReq);
}