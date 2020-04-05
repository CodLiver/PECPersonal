package ejbholidaybookingapp;

import java.io.Serializable;

public class JmsAlertDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String reqname;

	public JmsAlertDTO(int id, String reqname) {
		this.id = id;
		this.reqname = reqname;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReqname() {
		return reqname;
	}

	public void setReqname(String name) {
		this.reqname = reqname;
	}


}