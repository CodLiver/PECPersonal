package ejbholidaybookingapp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.TDepartment;
import model.TEmployee;
import model.TEmployeeRole;

/**
 * Session Bean implementation class HolidayBookingAppEJB
 */
@Stateless
@LocalBean
public class HolidayBookingAppBean implements HolidayBookingAppBeanRemote {

	/**
	 * Default constructor.
	 */
	public HolidayBookingAppBean() {
		// TODO Auto-generated constructor stub
	}

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public boolean login(String email, String password) {
		List queryResults = entityManager
				.createQuery("SELECT e FROM TEmployee e WHERE e.email = :email and e.password = :password and (e.employeeRole = 0 or e.employeeRole = 1)")
				.setParameter("email", email).setParameter("password", password).getResultList();
		//0 or 1 to check access privileges
		if (queryResults.isEmpty())
			return false;
		else
			return true;
	}
	
	@Override
	public EmployeeDTO getEmployeeById(int id) {
		List<TEmployee> queryResult = entityManager
				.createQuery("SELECT e FROM TEmployee e WHERE e.id = :id")
				.setParameter("id", id).getResultList();
		List<EmployeeDTO> employeeDTO = new ArrayList<>();
		for (TEmployee e : queryResult) {
			employeeDTO.add(new EmployeeDTO(e.getId(), e.getEmployeeRole().getIdEmpRole(), e.getEmployeeRole().getEmpRoleName(),
					e.getDepartment().getIdDep(), e.getDepartment().getDepartmentName(),
					e.getEmail(), e.getPassword(), e.getFirstName(), e.getLastName(), e.getPhoneNumber(),
					e.getHireDate(), e.getSalary(), e.getHomeAddress()));
		}
		return employeeDTO.get(0);
	}

	@Override
	public EmployeeDTO getEmployeeByEmail(String email) {
		List<TEmployee> queryResult = entityManager
				.createQuery("SELECT e FROM TEmployee e WHERE e.email = :email")
				.setParameter("email", email).getResultList();
		List<EmployeeDTO> employeeDTO = new ArrayList<>();
		for (TEmployee e : queryResult) {
			employeeDTO.add(new EmployeeDTO(e.getId(), e.getEmployeeRole().getIdEmpRole(), e.getEmployeeRole().getEmpRoleName(),
					e.getDepartment().getIdDep(), e.getDepartment().getDepartmentName(),
					e.getEmail(), e.getPassword(), e.getFirstName(), e.getLastName(), e.getPhoneNumber(),
					e.getHireDate(), e.getSalary(), e.getHomeAddress()));
		}
		return employeeDTO.get(0);
	}

	@Override
	public List<DepartmentDTO> getDepartments() {
		List<TDepartment> departments = entityManager.createNamedQuery("TDepartment.findAll").getResultList();
		List<DepartmentDTO> departmentsDTO = new ArrayList<>();
		for (TDepartment e : departments) {
			departmentsDTO.add(new DepartmentDTO(e.getIdDep(), e.getDepartmentName()));
		}
		return departmentsDTO;
	}

	@Override
	public List<EmployeeRoleDTO> getRoles() {
		List<TEmployeeRole> employeeRoles = entityManager.createNamedQuery("TEmployeeRole.findAll").getResultList();
		List<EmployeeRoleDTO> employeeRolesDTO = new ArrayList<>();
		for (TEmployeeRole e : employeeRoles) {
			employeeRolesDTO.add(new EmployeeRoleDTO(e.getIdEmpRole(), e.getEmpRoleName(), e.getCanApprove()));
		}
		return employeeRolesDTO;
	}

	@Override
	public List<EmployeeDTO> getAllEmployees() {
		List<TEmployee> allEmployees = entityManager.createNamedQuery("TEmployee.findAll").getResultList();
		List<EmployeeDTO> allEmployeesDTO = new ArrayList<>();
		for (TEmployee e : allEmployees) {
			allEmployeesDTO.add(new EmployeeDTO(e.getId(), e.getEmployeeRole().getIdEmpRole(), e.getEmployeeRole().getEmpRoleName(),
					e.getDepartment().getIdDep(), e.getDepartment().getDepartmentName(),
					e.getEmail(), e.getPassword(), e.getFirstName(), e.getLastName(), e.getPhoneNumber(),
					e.getHireDate(), e.getSalary(), e.getHomeAddress()));
		}
		return allEmployeesDTO;
	}

	@Override
	public boolean addNewEmployee(EmployeeDTO newEmp) {
		try {
			TEmployeeRole employeeRole = entityManager.find(TEmployeeRole.class, newEmp.getEmpRoleId());
			TDepartment department = entityManager.find(TDepartment.class, newEmp.getDepId());
			TEmployee newEmployee = new TEmployee(newEmp.getEmail(), newEmp.getPassword(), newEmp.getFirstName(),
					newEmp.getLastName(), newEmp.getPhoneNumber(), newEmp.getHireDate(), newEmp.getSalary(),
					newEmp.getHomeAddress(), department, employeeRole);
			entityManager.persist(newEmployee);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean editEmployee(EmployeeDTO updateEmp) {
		try {
			TEmployeeRole employeeRole = entityManager.find(TEmployeeRole.class, updateEmp.getEmpRoleId());
			TDepartment department = entityManager.find(TDepartment.class, updateEmp.getDepId());
			TEmployee employee = entityManager.find(TEmployee.class, updateEmp.getId());

			employee.setEmail(updateEmp.getEmail());
			employee.setPassword(updateEmp.getPassword());
			employee.setFirstName(updateEmp.getFirstName());
			employee.setLastName(updateEmp.getLastName());
			employee.setPhoneNumber(updateEmp.getPhoneNumber());
			employee.setHireDate(updateEmp.getHireDate());
			employee.setSalary(updateEmp.getSalary());
			employee.setHomeAddress(updateEmp.getHomeAddress());
			employee.setDepartment(department);
			employee.setEmployeeRole(employeeRole);

			entityManager.merge(employee);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteEmployee(EmployeeDTO deleteEmp) {
		try {
			TEmployee employee = entityManager.find(TEmployee.class, deleteEmp.getId());
			entityManager.remove(employee);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}