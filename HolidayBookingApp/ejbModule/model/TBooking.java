package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name = "holiday_booking")
@NamedQuery(name = "TBooking.findAll", query = "SELECT b FROM TBooking b")
public class TBooking implements Serializable {
	private static final long serialVersionUID = 1L; 

	public TBooking(String begin_date, String end_date, int duration, int holiday_remaining, TEmployee id_emp, TRequest id_req,
			TDepartment id_dep) {
		//maybe the need of TEmployee id_emp
		this.begin_date = begin_date;
		this.end_date = end_date;
		this.duration = duration;
		this.holiday_remaining = holiday_remaining;
		this.id_emp = id_emp;
		this.id_req = id_req;
		this.id_dep = id_dep;
	}
	
	public TBooking() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_BOOKING")
	private int Id;
	
	@Column(name = "BOOKING_BEGIN_DATE")
	private String begin_date;
	
	@Column(name = "BOOKING_END_DATE")
	private String end_date;
	
	@Column(name = "BOOKING_DURATION")
	private int duration;
	
	@Column(name = "CONF_HOL_REM")
	private int holiday_remaining;
	
	//@Column(name = "ID_EMP")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_EMP", nullable = false)
	private TEmployee id_emp;
	//add class change
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_REQ", nullable = false)
	private TRequest id_req;
	//add class change
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_DEP", nullable = false)
	private TDepartment id_dep;
	//add class change
	
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
	
	public String getBegin_date() {
		return begin_date;
	}
	
	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}
	
	public String getEnd_date() {
		return end_date;
	}
	
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getHoliday_remaining() {
		return holiday_remaining;
	}

	public void setHoliday_remaining(int holiday_remaining) {
		this.holiday_remaining = holiday_remaining;
	}

	public TEmployee getId_emp() {
		return id_emp;
	}

	public void setId_emp(TEmployee id_emp) {
		this.id_emp = id_emp;
	}
	
	public TRequest getRequest() {
		//maybe inside changes.
		return id_req;
	}

	public void setRequest(TRequest id_req) {
		this.id_req = id_req;
	}

	public TDepartment getDepartment() {
		return id_dep;
	}

	public void setDepartment(TDepartment id_dep) {
		this.id_dep = id_dep;
	}
	
}
