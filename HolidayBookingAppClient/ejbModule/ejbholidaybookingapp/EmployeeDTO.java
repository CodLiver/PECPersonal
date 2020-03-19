package ejbholidaybookingapp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class EmployeeDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int Id;
	private int empRoleId;
	private String empRoleName;
	private int depId;
	private String depName;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String hireDate;
	private int holiday_entitlement;
	private Integer salary;
	private String homeAddress;

	public EmployeeDTO(int id, int empRoleId, String empRoleName, int depId, String depName, String email, String password, String firstName,
			String lastName, String phoneNumber, String hireDate, int holiday_entitlement, Integer salary, String homeAddress) {
		this.Id = id;
		this.empRoleId = empRoleId;
		this.empRoleName = empRoleName;
		this.depId = depId;
		this.depName = depName;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.hireDate = hireDate;
		this.holiday_entitlement = holiday_entitlement;
		this.salary = salary;
		this.homeAddress = homeAddress;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
	
	public int getEmpRoleId() {
		return empRoleId;
	}
	
	public void setEmpRoleId(int empRoleId) {
		this.empRoleId = empRoleId;
	}
	
	public String getEmpRoleName() {
		return empRoleName;
	}
	
	public void setEmpRoleName(String empRoleName) {
		this.empRoleName = empRoleName;
	}

	public int getDepId() {
		return depId;
	}

	public void setDepId(int depId) {
		this.depId = depId;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

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

}