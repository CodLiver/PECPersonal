package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_DEPARTMENTS database table.
 * 
 */
@Entity
@Table(name="employee_roles")
@NamedQuery(name="TEmployeeRole.findAll", query="SELECT t FROM TEmployeeRole t")
public class TEmployeeRole implements Serializable {
	private static final long serialVersionUID = 1L;

	public TEmployeeRole(int id, String empRoleName, boolean canApprove) {
		this.id = id;
		this.name = empRoleName;
		this.canApprove = canApprove;
	}

	public TEmployeeRole() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_EMP_ROLE")
	private int id;

	@Column(name="EMP_ROLE_NAME")
	private String name;

	@Column(name="CAN_APPROVE")
	private boolean canApprove;

	public int getIdEmpRole() {
		return this.id;
	}

	public void setIdEmpRole(int idEmpRole) {
		this.id = idEmpRole;
	}

	public String getEmpRoleName() {
		return this.name;
	}

	public void setEmpRoleName(String empRoleName) {
		this.name = empRoleName;
	}

	public boolean getCanApprove() {
		return this.canApprove;
	}

	public void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove;
	}
}