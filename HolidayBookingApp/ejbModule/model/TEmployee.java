package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The persistent class for the T_EMPLOYEE database table.
 * 
 */
@Entity
@Table(name = "employee")
@NamedQuery(name = "TEmployee.findAll", query = "SELECT t FROM TEmployee t")
public class TEmployee implements Serializable {
	private static final long serialVersionUID = 1L;

	public TEmployee(String email, String password, String firstName, String lastName, String phoneNumber,
			String hireDate, int holiday_entitlement, Integer salary, String homeAddress, TDepartment department, TEmployeeRole employeeRole) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.hireDate = hireDate;
		this.holiday_entitlement = holiday_entitlement;
		this.salary = salary;
		this.homeAddress = homeAddress;
		this.department = department;
		this.employeeRole = employeeRole;
	}

	public TEmployee() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_EMP")
	private int Id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_DEP", nullable = false)
	private TDepartment department;

	public TDepartment getDepartment() {
		return department;
	}

	public void setDepartment(TDepartment department) {
		this.department = department;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_EMP_ROLE", nullable = false)
	private TEmployeeRole employeeRole;

	public TEmployeeRole getEmployeeRole() {
		return employeeRole;
	}

	public void setEmployeeRole(TEmployeeRole employeeRole) {
		this.employeeRole = employeeRole;
	}

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "HIRE_DATE")
	private String hireDate;
	
	@Column(name = "HOL_DAYS_ENTITLEMENT")
	private int holiday_entitlement;

	@Column(name = "SALARY")
	private Integer salary;

	@Column(name = "HOME_ADDRESS")
	private String homeAddress;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}
	
	public int getHoliday_entitlement() {
		return holiday_entitlement;
	}

	public void setHoliday_entitlement(int holiday_entitlement) {
		this.holiday_entitlement = holiday_entitlement;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}