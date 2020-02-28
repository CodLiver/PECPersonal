 
package ejbholidaybookingapp;

import java.io.Serializable;

public class EmployeeRoleDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private boolean canApprove;

	public EmployeeRoleDTO(int id, String name, boolean canApprove) {
		this.id = id;
		this.name = name;
		this.canApprove = canApprove;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return name;
	}

	public void setRoleName(String name) {
		this.name = name;
	}

	public boolean getCanApprove() {
		return canApprove;
	}

	public void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove;
	}

}