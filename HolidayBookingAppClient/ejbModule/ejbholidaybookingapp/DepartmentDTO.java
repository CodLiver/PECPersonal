  
package ejbholidaybookingapp;

import java.io.Serializable;

public class DepartmentDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int idDep;
	private String depName;

	public DepartmentDTO(int id, String name) {
		this.idDep = id;
		this.depName = name;
	}

	public int getIdDep() {
		return this.idDep;
	}

	public void setIdDep(int id) {
		this.idDep = id;
	}

	public String getDepartmentName() {
		return this.depName;
	}

	public void setDepartmentName(String name) {
		this.depName = depName;
	}

}