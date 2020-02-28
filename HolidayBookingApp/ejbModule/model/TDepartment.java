package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_DEPARTMENTS database table.
 * 
 */
@Entity
@Table(name="departments")
@NamedQuery(name="TDepartment.findAll", query="SELECT t FROM TDepartment t")
public class TDepartment implements Serializable {
	private static final long serialVersionUID = 1L;

	public TDepartment(int id, String name, int maxPeople, int requiredPeople) {
		this.idDep = id;
		this.depName = name;
		this.maxNPeople = maxPeople;
		this.nRequiredPeople = requiredPeople;
	}

	public TDepartment() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID_DEP")
	private int idDep;

	@Column(name="DEP_NAME")
	private String depName;

	@Column(name="MAX_N_PEOPLE")
	private int maxNPeople;

	@Column(name="N_REQUIRED_PEOPLE")
	private int nRequiredPeople;

	public int getIdDep() {
		return this.idDep;
	}

	public void setIdDep(int idDep) {
		this.idDep = idDep;
	}

	public String getDepartmentName() {
		return this.depName;
	}

	public void setDepartmentName(String depName) {
		this.depName = depName;
	}

	public int getMaxNPeople() {
		return this.maxNPeople;
	}

	public void setMaxNPeople(int maxNPeople) {
		this.maxNPeople = maxNPeople;
	}

	public int getNRequiredPeople() {
		return this.nRequiredPeople;
	}

	public void setNRequiredPeople(int nRequiredPeople) {
		this.nRequiredPeople = nRequiredPeople;
	}

}