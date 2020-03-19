package ejbholidaybookingapp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class BookingDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int Id;
	private String begin_date;
	private String end_date;
	private int duration;
	private int holiday_remaining;
	private int id_emp;
	private int id_req;
	private int id_dep;

	public BookingDTO(int id, String begin_date, String end_date, int duration, int holiday_remaining, int id_emp , int id_req,
			int id_dep) {
		this.Id = id;
		this.begin_date = begin_date;
		this.end_date = end_date;
		this.duration = duration;
		this.holiday_remaining = holiday_remaining;
		this.id_emp = id_emp ;
		this.id_req = id_req;
		this.id_dep = id_dep;
	}

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

	public int getId_emp() {
		return id_emp;
	}

	public void setId_emp(int id_emp) {
		this.id_emp = id_emp;
	}

	public int getId_Req() {
		//maybe inside changes.
		return id_req;
	}

	public void setId_Req(int id_req) {
		this.id_req = id_req;
	}

	public int getId_Dep() {
		return id_dep;
	}

	public void setId_Dep(int id_dep) {
		this.id_dep = id_dep;
	}
}
