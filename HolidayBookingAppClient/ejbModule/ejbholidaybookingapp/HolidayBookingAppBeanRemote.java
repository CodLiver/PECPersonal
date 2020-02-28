package ejbholidaybookingapp;

import java.util.List;
import javax.ejb.Remote;

@Remote
public interface HolidayBookingAppBeanRemote {
	boolean login(String email, String password);
	EmployeeDTO getEmployeeByEmail(String email);
	EmployeeDTO getEmployeeById(int id);
	List<EmployeeDTO> getAllEmployees();
	List<DepartmentDTO> getDepartments();
	List<EmployeeRoleDTO> getRoles();
	boolean addNewEmployee(EmployeeDTO newEmp);
	boolean editEmployee(EmployeeDTO updateEmp);
	boolean deleteEmployee(EmployeeDTO deleteEmp);
}