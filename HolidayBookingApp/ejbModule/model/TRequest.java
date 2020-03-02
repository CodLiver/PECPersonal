package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name = "holiday_request")
@NamedQuery(name = "TRequest.findAll", query = "SELECT r FROM TRequest r")
public class TRequest implements Serializable {
	private static final long serialVersionUID = 1L; 

	public TRequest(String begin_date, String end_date, int duration, int holiday_entitlement, int holiday_remaining, TEmployee id_emp, int peak_time,
			int status) {
		//maybe the need of TEmployee id_emp
		this.begin_date = begin_date;
		this.end_date = end_date;
		this.duration = duration;
		this.holiday_entitlement = holiday_entitlement;
		this.holiday_remaining = holiday_remaining;
		this.id_emp = id_emp;
		this.peak_time = peak_time;
		this.status = status;
	}
	
	public TRequest() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_REQ")
	private int Id;
	
	@Column(name = "BEGIN_DATE")
	private String begin_date;
	
	@Column(name = "END_DATE")
	private String end_date;
	
	@Column(name = "DURATION")
	private int duration;
	
	@Column(name = "HOL_DAYS_ENTITLEMENT")
	private int holiday_entitlement;
	
	@Column(name = "HOL_DAYS_REMAINING")
	private int holiday_remaining;
	
	//@Column(name = "ID_EMP")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_EMP", nullable = false)
	private TEmployee id_emp;
	//add class change
	
	//@ManyToOne(fetch = FetchType.EAGER)
	//@JoinColumn(name = "ID_PEAK_TIME", nullable = false)
	@Column(name = "ID_PEAK_TIME")
	private int peak_time;
	//add class change
	

	//@ManyToOne(fetch = FetchType.EAGER)
	//@JoinColumn(name = "ID_STATUS", nullable = false)
	@Column(name = "ID_STATUS")
	private int status;
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

	public int getHoliday_entitlement() {
		return holiday_entitlement;
	}

	public void setHoliday_entitlement(int holiday_entitlement) {
		this.holiday_entitlement = holiday_entitlement;
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
	
	public int getPeak_time() {
		//maybe inside changes.
		return peak_time;
	}

	public void setPeak_time(int peak_time) {
		this.peak_time = peak_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
