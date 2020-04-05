package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the T_DEPARTMENTS database table.
 * 
 */
@Entity
@Table(name="jmsalert")
@NamedQuery(name="TJmsAlert.findAll", query="SELECT j FROM TJmsAlert j")
public class TJmsAlert implements Serializable {
	private static final long serialVersionUID = 1L;

	public TJmsAlert(String reqname) {
		this.reqname = reqname;
	}

	public TJmsAlert() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="Id")
	private int id;

	@Column(name="Reqname")
	private String reqname;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReqname() {
		return this.reqname;
	}

	public void setReqname(String reqname) {
		this.reqname = reqname;
	}

}