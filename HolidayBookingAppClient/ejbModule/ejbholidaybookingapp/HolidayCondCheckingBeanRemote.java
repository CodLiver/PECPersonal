package ejbholidaybookingapp;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Remote
public interface HolidayCondCheckingBeanRemote {
	
	int holidayRemainingCalc(List<BookingDTO> listofAllDepBooking, String beginDate, String endDate, EmployeeDTO thatEmployee);
	int dateDiffCalc(Date leftDate,Date rightDate);
	int holidayDeductCalc(Date beginDate,Date endDate,Date nxMassSDate,Date nxMassEDate);
	int xMassCheck(String beginDateStr, String endDateStr);
	int getMaxCanClashEmp(List<EmployeeDTO> allEmpListPerDep,boolean isPeak);
	List<BookingDTO> getClashingBookingList(Date startDate,Date finDate, EmployeeDTO thatEmployee, List<BookingDTO> listofAllDepBooking);
	//int[] calculateHolClashesAndConflicts(int[] condBreakerList, List<BookingDTO> clashingBookingList, EmployeeDTO thatEmployee, int maxCanClash, int roleConflictCount, int reqCheck_condition);
	int[] holAllowanceForHeadDep(Date startDate,Date finDate, List<BookingDTO> listofAllDepBooking, EmployeeDTO thatEmployee,List<EmployeeDTO> allEmpListPerDep);
	int[] holAllowanceForManSen(Date startDate,Date finDate, List<BookingDTO> listofAllDepBooking, EmployeeDTO thatEmployee,List<EmployeeDTO> allEmpListPerDep);
	int[] holAllowanceForNonSpecs(Date startDate,Date finDate, List<BookingDTO> listofAllDepBooking, EmployeeDTO thatEmployee,List<EmployeeDTO> allEmpListPerDep);
	List<Object> getAllOutstandingRequestsByDep(int id_dep);
	List<List<EmployeeDTO>> workingFilters(String email,String beginRange, String endRange);
	
	
}
