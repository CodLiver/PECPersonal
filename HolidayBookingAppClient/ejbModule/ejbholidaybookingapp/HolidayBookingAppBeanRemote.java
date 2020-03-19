package ejbholidaybookingapp;

import java.util.List;
import javax.ejb.Remote;

@Remote
public interface HolidayBookingAppBeanRemote {
	int login(String email, String password);
	List<RequestDTO> getAllRequestperEmp(String email);
	EmployeeDTO getEmployeeByEmail(String email);
	EmployeeDTO getEmployeeById(int id);
	List<BookingDTO> getAllBookingsperEmp(String email);
	List<EmployeeDTO> getAllEmployees();
	List<DepartmentDTO> getDepartments();
	List<EmployeeRoleDTO> getRoles();
	List<BookingDTO> getAllBookings();
	boolean addNewEmployee(EmployeeDTO newEmp);
	boolean editEmployee(EmployeeDTO updateEmp);
	boolean deleteEmployee(EmployeeDTO deleteEmp);
	boolean addNewRequest(RequestDTO newReq);
}