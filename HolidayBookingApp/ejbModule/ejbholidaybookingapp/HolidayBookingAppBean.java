package ejbholidaybookingapp;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;

import model.TBooking;
import model.TDepartment;
import model.TEmployee;
import model.TEmployeeRole;
import model.TRequest;

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
	public int login(String email, String password) {
		
		System.out.println(email+password);
		
		List queryResultsHead = entityManager
				.createQuery("SELECT e FROM TEmployee e WHERE e.email = :email and e.password = :password and (e.employeeRole < 2)")
				.setParameter("email", email).setParameter("password", password).getResultList();
		//0 or 1 to check access privileges
		if (queryResultsHead.isEmpty()) {
			List queryResultsEmp = entityManager
			.createQuery("SELECT e FROM TEmployee e WHERE e.email = :email and e.password = :password and (e.employeeRole >= 2)")
			.setParameter("email", email).setParameter("password", password).getResultList();
			
			if (queryResultsEmp.isEmpty()) {
				return 0;
			}
			else {
				return 2;
			}
		}
		else 
		{
			return 1;
		}
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
					e.getHireDate(),e.getHoliday_entitlement(), e.getSalary(), e.getHomeAddress()));
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
					e.getHireDate(),e.getHoliday_entitlement(), e.getSalary(), e.getHomeAddress()));
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
					e.getHireDate(),e.getHoliday_entitlement(), e.getSalary(), e.getHomeAddress()));
		}
		return allEmployeesDTO;
	}
	
	@Override
	public List<EmployeeDTO> getAllEmployeesByDep(int id_dep) {
		List<TEmployee> allEmployees = entityManager.createNamedQuery("TEmployee.findAll").getResultList();
		List<EmployeeDTO> allEmployeesDTO = new ArrayList<>();
		for (TEmployee e : allEmployees) {
			
			if(e.getDepartment().getIdDep()==id_dep)
				allEmployeesDTO.add(new EmployeeDTO(e.getId(), e.getEmployeeRole().getIdEmpRole(), e.getEmployeeRole().getEmpRoleName(),
						e.getDepartment().getIdDep(), e.getDepartment().getDepartmentName(),
						e.getEmail(), e.getPassword(), e.getFirstName(), e.getLastName(), e.getPhoneNumber(),
						e.getHireDate(),e.getHoliday_entitlement(), e.getSalary(), e.getHomeAddress()));
		}
		return allEmployeesDTO;
	}
	
	@Override
	public List<BookingDTO> getAllBookings() {
		List<TBooking> allBookings= entityManager.createNamedQuery("TBooking.findAll").getResultList();
		List<BookingDTO> allBookingsDTO = new ArrayList<>();
		for (TBooking b : allBookings) {
			allBookingsDTO.add(new BookingDTO(b.getId(), b.getBegin_date(), b.getEnd_date(), b.getDuration(), b.getHoliday_remaining(),b.getId_emp().getId() , b.getRequest().getId(),
			b.getDepartment().getIdDep()));

		}
		return allBookingsDTO;
	}
	
	@Override
	public List<BookingDTO> getAllBookingsByDep(int dep_id) {
		List<TBooking> allBookings= entityManager.createNamedQuery("TBooking.findAll").getResultList();
		List<BookingDTO> allBookingsDTO = new ArrayList<>();
		for (TBooking b : allBookings) {	
			if (dep_id == b.getDepartment().getIdDep())
				allBookingsDTO.add(new BookingDTO(b.getId(), b.getBegin_date(), b.getEnd_date(), b.getDuration(), b.getHoliday_remaining(),b.getId_emp().getId() , b.getRequest().getId(),
				b.getDepartment().getIdDep()));
		}
		return allBookingsDTO;
	}
	
	@Override
	public List<BookingDTO> getAllBookingsperEmp(String email) {
		EmployeeDTO thatEmployee = getEmployeeByEmail(email);
		List<TBooking> allBookings= entityManager.createNamedQuery("TBooking.findAll").getResultList();
		List<BookingDTO> allBookingsDTO = new ArrayList<>();
		for (TBooking b : allBookings) {
			if (thatEmployee.getId() == b.getId_emp().getId())
				allBookingsDTO.add(new BookingDTO(b.getId(), b.getBegin_date(), b.getEnd_date(), b.getDuration(), b.getHoliday_remaining(),b.getId_emp().getId() , b.getRequest().getId(),
				b.getDepartment().getIdDep()));
		}
		return allBookingsDTO;
	}
	
	
	
	@Override
	public RequestDTO getRequestById(int req_id) {
		
		List<TRequest> allRequests = entityManager.createNamedQuery("TRequest.findAll").getResultList();
		List<RequestDTO> allRequestsDTO = new ArrayList<>();
		for (TRequest r : allRequests) {
			if (req_id == r.getId())
				allRequestsDTO.add(new RequestDTO(r.getId(), r.getBegin_date(), r.getEnd_date(), r.getDuration(), r.getHoliday_remaining(),r.getId_emp().getId() , r.getPeak_time(),
						r.getStatus()));

		}
		return allRequestsDTO.get(0);
		
	}
	
	@Override
	public List<RequestDTO> getAllRequestperEmp(String email) {
		EmployeeDTO thatEmployee = getEmployeeByEmail(email);
		List<TRequest> allRequests = entityManager.createNamedQuery("TRequest.findAll").getResultList();
		List<RequestDTO> allRequestsDTO = new ArrayList<>();
		for (TRequest r : allRequests) {
			if (thatEmployee.getId() == r.getId_emp().getId())
				allRequestsDTO.add(new RequestDTO(r.getId(), r.getBegin_date(), r.getEnd_date(), r.getDuration(), r.getHoliday_remaining(),r.getId_emp().getId() , r.getPeak_time(),
						r.getStatus()));

		}
		return allRequestsDTO;
	}
	
	@Override
	public List<RequestDTO> getAllRequestByDep(int id_dep) {
		List<TRequest> allRequests = entityManager.createNamedQuery("TRequest.findAll").getResultList();
		
		
		
		List<RequestDTO> allRequestsByDepDTO = new ArrayList<>();
		for (TRequest r : allRequests) {
			EmployeeDTO thatEmployee = getEmployeeById(r.getId_emp().getId());
			if (thatEmployee.getDepId() == id_dep)
				allRequestsByDepDTO.add(new RequestDTO(r.getId(), r.getBegin_date(), r.getEnd_date(), r.getDuration(), r.getHoliday_remaining(),r.getId_emp().getId() , r.getPeak_time(),
						r.getStatus()));

		}
		return allRequestsByDepDTO;
	}

	@Override
	public boolean addNewEmployee(EmployeeDTO newEmp) {
		try {
			TEmployeeRole employeeRole = entityManager.find(TEmployeeRole.class, newEmp.getEmpRoleId());
			TDepartment department = entityManager.find(TDepartment.class, newEmp.getDepId());
			TEmployee newEmployee = new TEmployee(newEmp.getEmail(), newEmp.getPassword(), newEmp.getFirstName(),
					newEmp.getLastName(), newEmp.getPhoneNumber(), newEmp.getHireDate(), newEmp.getHoliday_entitlement(), newEmp.getSalary(),
					newEmp.getHomeAddress(), department, employeeRole);
			entityManager.persist(newEmployee);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean addNewRequest(RequestDTO newReq) {
		try {			
			TEmployee employee_id = entityManager.find(TEmployee.class, newReq.getId_emp());
			//TEmployeeRole employeeRole = entityManager.find(TEmployeeRole.class, newEmp.getEmpRoleId());
			//TDepartment department = entityManager.find(TDepartment.class, newEmp.getDepId());
			TRequest newRequest = new TRequest(
					newReq.getBegin_date(), newReq.getEnd_date(), newReq.getDuration(),
					newReq.getHoliday_remaining(), employee_id, newReq.getPeak_time(),
					newReq.getStatus());
			entityManager.persist(newRequest);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean addRequestToBooking(RequestDTO requestBooked) {
		System.out.println("adding area");
		try {			
			TEmployee employee_id = entityManager.find(TEmployee.class, requestBooked.getId_emp());
			TRequest request_id = entityManager.find(TRequest.class, requestBooked.getId());
			TDepartment department_id = entityManager.find(TDepartment.class, employee_id.getDepartment().getIdDep());
			
			TBooking newBooking = new TBooking(
					requestBooked.getBegin_date(), requestBooked.getEnd_date(), requestBooked.getDuration(),
					requestBooked.getHoliday_remaining(), employee_id, request_id, department_id);
			entityManager.persist(newBooking);
			
			System.out.println("addded new booking");
			
			request_id.setStatus(1);
			entityManager.merge(request_id);
			
			System.out.println("updated req status");
			
			List<TRequest> allRequests = entityManager.createNamedQuery("TRequest.findAll").getResultList();
			
			System.out.println();
			
			for (TRequest r : allRequests) {
				if (requestBooked.getId_emp()==r.getId_emp().getId()&&r.getStatus()==2) {			
					int strLen=r.getBegin_date().length();
					
					String rIDbegin=r.getBegin_date().substring(strLen-4, strLen);
					String reqIDbegin=request_id.getBegin_date().substring(strLen-4, strLen);
					
					System.out.println("strLen matches");
					System.out.println(rIDbegin);
					System.out.println(reqIDbegin);
					System.out.println(r.getStatus());
					System.out.println(rIDbegin.equals(reqIDbegin));
					if(rIDbegin.equals(reqIDbegin)) {
						System.out.println("matched");
						System.out.println(request_id.getHoliday_remaining());					
						System.out.println(r.getHoliday_remaining());
						r.setHoliday_remaining(requestBooked.getHoliday_remaining()-r.getDuration());
						entityManager.merge(r);
					}
						
						
					
					
					System.out.println("updating reqs");
				}

			}
			
			
			/*
			 * 
			 * gather all requests per emp where pending==2
			 * update their hol remaining area with the new remaining.
			 * be careful, has to be in the same year area.
			 * 
			 * */
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	@Override
	public boolean rejectRequest(RequestDTO rejectRequest) {
		try {
			TRequest request = entityManager.find(TRequest.class, rejectRequest.getId());
			
			request.setStatus(0);

			entityManager.merge(request);
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
			employee.setHoliday_entitlement(updateEmp.getHoliday_entitlement());
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
	
	public boolean deleteRequest(RequestDTO deleteReq) {
		try {
			TRequest request = entityManager.find(TRequest.class, deleteReq.getId());
			entityManager.remove(request);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}