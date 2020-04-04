package ejbholidaybookingapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.TBooking;
import model.TDepartment;
import model.TEmployee;
import model.TEmployeeRole;
import model.TRequest;

//things have to be in T?
/**
 * Session Bean implementation class HolidayBookingAppEJB
 */
@Stateless
@LocalBean
public class HolidayCondCheckingBean implements HolidayCondCheckingBeanRemote {

	/**
	 * Default constructor.
	 */
	public HolidayCondCheckingBean() {
		// TODO Auto-generated constructor stub
	}
	
	@EJB
	private HolidayBookingAppBeanRemote holidayBookingAppBean;
	
	
	@Override
	public int holidayRemainingCalc(List<BookingDTO> listofAllDepBooking, String beginDate, String endDate, EmployeeDTO thatEmployee) {
		int remaining;
		System.out.println("holidayRemainingCalc");
		//may change to bookperemp later
		
		if (!listofAllDepBooking.isEmpty()) {//if employee has no booking
			List<BookingDTO> listofEmpBookingThatYear= new ArrayList<>();
			//-System.out.println("Idk whats going on but not good things");
			for (BookingDTO b : listofAllDepBooking) {//.getId_emp().getId()
				//-System.out.println(b.getBegin_date().substring(6, 10));
				//-System.out.println(beginDate.substring(0, 4));
				//-System.out.println(thatEmployee.getId());
				//-System.out.println(b.getId_emp());
				//-System.out.println("---");
				if((b.getBegin_date().substring(6, 10).equals(beginDate.substring(0, 4)))&&(thatEmployee.getId() == b.getId_emp())) {
					listofEmpBookingThatYear.add(b);
				}else if((b.getBegin_date().substring(6, 10).equals(beginDate.substring(6, 10)))&&(thatEmployee.getId() == b.getId_emp())) {
					listofEmpBookingThatYear.add(b);
				}
			}
			
			
			//-System.out.println("If there are bookings!");
			//-System.out.println(listofEmpBookingThatYear.size());
			//-System.out.println("Checked Emp");
			//-System.out.println(thatEmployee.getId());
			//-System.out.println(beginDate);
			//-System.out.println("???");
			
			if (listofEmpBookingThatYear.isEmpty()) {//if emp does not have that year booking. done to check irregular updates
				remaining=thatEmployee.getHoliday_entitlement();
			}else {
				BookingDTO minBook = listofEmpBookingThatYear.get(listofEmpBookingThatYear.size() - 1);
				
				for (BookingDTO b : listofEmpBookingThatYear) {//.getId_emp().getId()
					if(minBook.getHoliday_remaining()>b.getHoliday_remaining())
						minBook=b;
				}
				
				
				remaining=minBook.getHoliday_remaining();
				//-System.out.println("bookchecked");
				//-System.out.println(remaining);
			}
				
		}else {
			remaining=thatEmployee.getHoliday_entitlement();
		}
		
		return remaining;
	}
	
	//day calculation between 2 dates
	@Override
	public int dateDiffCalc(Date leftDate,Date rightDate) {
		
		long rightTime = rightDate.getTime();
		long leftTime = leftDate.getTime();  
		
		long diffTime = leftTime - rightTime;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		
		return (int) diffDays;
	}
	
	//collision day amount calculation between 2 date ranges.
	@Override
	public int holidayDeductCalc(Date beginDate,Date endDate,Date nxMassSDate,Date nxMassEDate) {
		
		//System.out.println(nxMassSDate);
		//System.out.println(nxMassEDate);
		
		//System.out.println("|xy|");
		if(beginDate.after(nxMassSDate)&&endDate.before(nxMassEDate))//|xy|  +-,+-
			return dateDiffCalc(endDate,beginDate);

		//System.out.println("xy||");
		if(endDate.before(nxMassSDate))//xy|| --,--
			return 0;
		
		//System.out.println("||xy");
		if(beginDate.after(nxMassEDate))//||xy ++,++
			return 0;
		
		//System.out.println("|x|y,x|y|");
		if(beginDate.after(nxMassSDate)) {//23/12--x----03/01---y   +-,++		
			
			//System.out.println("|x|y");
			return dateDiffCalc(nxMassEDate,beginDate);
			 
		}else if(endDate.before(nxMassEDate)&&endDate.after(nxMassSDate)&&beginDate.before(nxMassSDate)) {//x--|-y-|-   --,+-
			
			//System.out.println("x|y|");
			return dateDiffCalc(endDate,nxMassSDate); 
			 
		}else{//x--|---|--y   --,++
			//-System.out.println("x||y");
			return dateDiffCalc(nxMassEDate,nxMassSDate);
		}
		
	}
	
	//checks if date range in xmass range.
	@Override
	public int xMassCheck(String beginDateStr, String endDateStr) {
		
		//System.out.println("---------");
		
		int holDeductCalcing=0;
		try{
			
			int sYear;
			int eYear;
			try {
				sYear=Integer.parseInt(beginDateStr.substring(0, 4));
				eYear=Integer.parseInt(endDateStr.substring(0, 4));
				
			}catch(Exception e) {
				//-System.out.println("Accessed via Admin");
				sYear=Integer.parseInt(beginDateStr.substring(beginDateStr.length()-4, beginDateStr.length()));
				eYear=Integer.parseInt(endDateStr.substring(endDateStr.length()-4, endDateStr.length()));
			}

			int inCheck=eYear-sYear;
			
			Date beginDate;
			Date endDate;
			try {
				beginDate = new SimpleDateFormat("yyyy-MM-dd").parse(beginDateStr);
				endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateStr);
			}catch(Exception e) {
				//-System.out.println("Accessed via Admin");
				beginDate = new SimpleDateFormat("dd/MM/yyyy").parse(beginDateStr);
				endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDateStr);
			}

			
			if(inCheck==1) {	
				
					Date nxMassSDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(sYear)+"-12-23");
					Date nxMassEDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(eYear)+"-01-03");	

					//System.out.println("Diff Years");
					holDeductCalcing=holidayDeductCalc(beginDate,endDate,nxMassSDate,nxMassEDate);

			}else {
				
				/*
				 * 	2020-xmas()-2021-()xmas-2022
				 * 	
				 * 	both in early xmass 2020,2021
				 *	both in late xmass 2021,2022
				 * */
				for(int adder=0;adder<2;adder++) {
					Date nxMassSDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(sYear-1+adder)+"-12-23");
					Date nxMassEDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(eYear+adder)+"-01-03");	

					holDeductCalcing+=holidayDeductCalc(beginDate,endDate,nxMassSDate,nxMassEDate);
				}
				//System.out.println("Same Years");
			}
		
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		
		//-System.out.println(holDeductCalcing);
		return holDeductCalcing;	
		
	}
	
	//return max allowed clash (peak time not implemented yet.)
	@Override
	public int getMaxCanClashEmp(List<EmployeeDTO> allEmpListPerDep,boolean isPeak) {
		
		int totDepSize=allEmpListPerDep.size();
		double minDepSizeDouble=totDepSize*0.6;//check for 40%peak time rule too.
		if (!isPeak)
			minDepSizeDouble=totDepSize*0.4;
		int minDepSize=(int) Math.ceil(minDepSizeDouble);
		int maxCanClash=totDepSize-minDepSize;

		System.out.println("allowed area: "+String.valueOf(allEmpListPerDep.size())+"vs"+String.valueOf(maxCanClash));
		System.out.println("allowed area: "+isPeak);
		return maxCanClash;
	}
	
	//returns a list of clashing confirmed bookings for employee's date range.
	@Override
	public List<BookingDTO> getClashingBookingList(Date startDate,Date finDate, EmployeeDTO thatEmployee, List<BookingDTO> listofAllDepBooking){
		
		//check clashes with your request with OTHERS. and same title
		List<BookingDTO> clashingBookingList=new ArrayList<>();

		try {
			
			for (BookingDTO b : listofAllDepBooking) {
				
				if(b.getDuration()==0)
					continue;
				
				Date bStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(b.getBegin_date());
				Date bFinDate = new SimpleDateFormat("dd/MM/yyyy").parse(b.getEnd_date());
				
				int potentialClashDay=holidayDeductCalc(startDate,finDate,bStartDate,bFinDate);
				
				
				if(potentialClashDay!=0 && b.getId_emp()!=thatEmployee.getId()) { //{//get booking that not yours
					
					
					//del this deprecated
					Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
					cal.setTime(startDate);
					String syear=String.valueOf(cal.get(Calendar.YEAR));
					cal.setTime(finDate);
					String eyear=String.valueOf(cal.get(Calendar.YEAR));
					cal.setTime(bFinDate);
					String ebyear=String.valueOf(cal.get(Calendar.YEAR));
					cal.setTime(bStartDate);
					String sbyear=String.valueOf(cal.get(Calendar.YEAR));
					
					Date xmassSDate = new SimpleDateFormat("dd/MM/yyyy").parse("23/12/"+syear);
					Date xmassEDate = new SimpleDateFormat("dd/MM/yyyy").parse("03/01/"+ebyear);
					
					if (!syear.equals(ebyear)) {
						if (finDate.after(xmassSDate)&&finDate.before(xmassEDate)&&bStartDate.after(xmassSDate)&&bStartDate.before(xmassEDate)) {
							continue;
						}
					}//del this deprecated
					
					clashingBookingList.add(b);
					
					/*
					 * 
					 * dec         Jan
 					 *  ---{---|   }
 					 *     {---|---}---
 					 *     
 					 *     *********
 					 *  ---{   |   }
 					 *     {   |   }---
 					 *  if start1!=end2
 					 *  prob
 					 *  e1 after xmassstart&b4end   & s2-after-xmasSstart & before xmassend
 					 *  
					 * 
					 * Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
					cal.setTime(startDate);
					String syear=String.valueOf(cal.get(Calendar.YEAR));
					

					Date augStart = new SimpleDateFormat("dd/MM/yyyy").parse("23/12/"+syear);
					Date augEnd = new SimpleDateFormat("dd/MM/yyyy").parse("03/01/"+syear);
						
					int checkIfClashInxMass= holidayDeductCalc(startDate,finDate,augStart,augEnd);//check if the date touches aug.

						
					if (checkIfAug>0)
						return true;
					
					if(false)*/
						
				}
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		
		return clashingBookingList; 
		
	}
	
	//returns the condbreaker array for employee's request.
	//@Override
	private int[] calculateHolClashesAndConflicts(int[] condBreakerList, List<BookingDTO> clashingBookingList, EmployeeDTO thatEmployee, int maxCanClash, int roleConflictCount, int reqCheck_condition) {
		//System.out.println(String.valueOf(i));
		//System.out.println();
		try {
			for (int clashInd=0;clashInd<clashingBookingList.size();clashInd++) {//BookingDTO clashed : clashingBookingList) {
				int interRoleClashNo=0;
				//-System.out.println("For for's for.");
				
				BookingDTO curClash = clashingBookingList.get(clashInd);
				
				int clashEmpRole = holidayBookingAppBean.getEmployeeById(curClash.getId_emp()).getEmpRoleId();
				
				Date cb1s=new SimpleDateFormat("dd/MM/yyyy").parse(curClash.getBegin_date());
				Date cb1e=new SimpleDateFormat("dd/MM/yyyy").parse(curClash.getEnd_date());
				
				List<Integer> clashUniques=new ArrayList<>();
				
				for(int clashIndx=0;clashIndx<clashingBookingList.size();clashIndx++) {						
					//-System.out.println("For for's for for.");
					System.out.println("clashunix size "+String.valueOf(clashUniques.size()));
						
						BookingDTO curClashx = clashingBookingList.get(clashIndx);
						
						if(clashIndx==clashInd || curClashx.getId_emp()==curClash.getId_emp() || clashUniques.contains(curClashx.getId_emp()))
							continue;
						
							
						Date cb2s=new SimpleDateFormat("dd/MM/yyyy").parse(clashingBookingList.get(clashIndx).getBegin_date());
						Date cb2e=new SimpleDateFormat("dd/MM/yyyy").parse(clashingBookingList.get(clashIndx).getEnd_date());
						
						System.out.println("wtf dates: "+cb1s.toString()+""+cb1e.toString()+""+cb2s.toString()+""+cb2e.toString());
						
						int clashingDaysNo=holidayDeductCalc(cb1s,cb1e,cb2s,cb2e);//clash checking area
						//-System.out.println(clashingDaysNo);
						if(clashingDaysNo!=0) {//maybe add unix here too.
							clashUniques.add(curClashx.getId_emp());
							
							
							//-System.out.println("clashday!=0");
							
							
							int clashEmpRolex = holidayBookingAppBean.getEmployeeById(curClashx.getId_emp()).getEmpRoleId();
							
							//-System.out.println("land o rolex: "+String.valueOf(clashEmpRole)+"-x"+String.valueOf(clashEmpRolex));
							if (reqCheck_condition==1) {
								//-System.out.println("entered reqcheck 1");
								if(clashEmpRolex<2 && clashEmpRole<2)
									interRoleClashNo++;
								//-System.out.println("1 con: "+String.valueOf(interRoleClashNo));
								
							}else if(reqCheck_condition==2) {
								//-System.out.println("entered reqcheck 2");
								if(thatEmployee.getEmpRoleId()==clashEmpRolex && clashEmpRole==thatEmployee.getEmpRoleId())//mindful using with other non important roles.
									interRoleClashNo++;
								//-System.out.println("2 con: "+String.valueOf(interRoleClashNo));
							}
							
	
							//then check then reqs.fix this shit too^
							//how to check if manager/senior class eachother				
						}	
				}
				
				
				//-System.out.println("Roles: "+String.valueOf(clashEmpRole)+" v "+String.valueOf(thatEmployee.getEmpRoleId()));
				if (clashEmpRole<2&&thatEmployee.getEmpRoleId()<2) {
					interRoleClashNo++;
				}else if(clashEmpRole==thatEmployee.getEmpRoleId()&&reqCheck_condition==2) {
					interRoleClashNo++;
					interRoleClashNo++;
				}
				
				if (interRoleClashNo>=roleConflictCount&&reqCheck_condition==2) {
					//-System.out.println("cond 2 breaker added");
					//-System.out.println("roleclashno"+String.valueOf(interRoleClashNo)+" roleConf: "+String.valueOf(roleConflictCount));
					condBreakerList[2]=1;
					//request.setAttribute("errorLoginMessage", "You are the only available Experienced staff, you cannot leave your people!");
					//request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
				}else if(interRoleClashNo>=1&&reqCheck_condition==1) {
					//-System.out.println("cond 1 breaker added");
					//-System.out.println("roleclashno"+String.valueOf(interRoleClashNo)+" roleConf: "+String.valueOf(roleConflictCount));
					condBreakerList[1]=1;//roleConflictCount=2
					//request.setAttribute("errorLoginMessage", "You are the only available head master, you cannot leave your people!");
					//request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
				}
				
				
				System.out.println("cond3 unix+1 >= maxcl: "+String.valueOf(clashUniques.size()+1)+">="+String.valueOf(maxCanClash));
				if(clashUniques.size()+1>=maxCanClash) {//basis of 60% constraint
					System.out.println("cond 3 breaker added");
					condBreakerList[3]=1;
					//request.setAttribute("errorLoginMessage", "60% of the department should work.");
					//request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
				}
					
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return condBreakerList;
	}
	
	private boolean checkIfinAug(Date startDate,Date finDate) {
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
		cal.setTime(startDate);
		String syear=String.valueOf(cal.get(Calendar.YEAR));
		

		
		try {
			Date augStart = new SimpleDateFormat("dd/MM/yyyy").parse("01/08/"+syear);
			Date augEnd = new SimpleDateFormat("dd/MM/yyyy").parse("31/08/"+syear);
			
			int checkIfAug= holidayDeductCalc(startDate,finDate,augStart,augEnd);//check if the date touches aug.
			
			
			System.out.println("check if aug");
			System.out.println("Start Dates Checked: "+startDate.toString()+"-"+finDate.toString());
			System.out.println(checkIfAug);
			
			if (checkIfAug>0)
				return true;
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	
		return false;
	}
	
	@Override
	public int[] calcAugBreak(int[] condBreakerList,Date startDate, Date finDate, List<EmployeeDTO> allEmpListPerDep, List<BookingDTO> clashingBookingList,int emp_id) {
		
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
		cal.setTime(startDate);
		String syear=String.valueOf(cal.get(Calendar.YEAR));
		

		
		try {
			Date augStart = new SimpleDateFormat("dd/MM/yyyy").parse("01/08/"+syear);
			Date augEnd = new SimpleDateFormat("dd/MM/yyyy").parse("31/08/"+syear);
			
			int checkIfAug= holidayDeductCalc(startDate,finDate,augStart,augEnd);//check if the date touches aug.
			
			
			System.out.println("check if aug");
			System.out.println("Start Dates Checked: "+startDate.toString()+"-"+finDate.toString());
			System.out.println(checkIfAug);
			
			if (checkIfAug>0) {
				
				
				Object[] latestStart = {startDate,0};
				Object[] earliestFinish = {finDate,0};
				boolean isPartial=false;
				
				if(finDate.after(augEnd)) {
					//finDate=augEnd
					isPartial=true;
					earliestFinish[0]=augEnd;
				}
				
				if(startDate.before(augStart)) {
					//startDate=augStart
					latestStart[0]=augStart;
				}
			
				List<Date> getDates = new ArrayList<>();
				
				//String.valueOf(cal.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(cal.get(Calendar.MONTH))+"/"+String.valueOf(cal.get(Calendar.YEAR))
				getDates.add(startDate);
				//cal.setTime(finDate);
				getDates.add(finDate);
				
				int counter=1;
				for (BookingDTO b: clashingBookingList) {
					
					if (b.getId_emp()!=emp_id) {//not sure if good idea.

					Date in_beginDate = new SimpleDateFormat("dd/MM/yyyy").parse(b.getBegin_date());
					Date in_endDate = new SimpleDateFormat("dd/MM/yyyy").parse(b.getEnd_date());
					
					System.out.println("b Dates Checked: "+in_beginDate.toString()+"-"+in_endDate.toString());
					
					if (in_beginDate.after((Date) latestStart[0])) {
						System.out.println("this b date's start is after than latest start: "+String.valueOf(counter)+"-"+in_beginDate);
						latestStart[0]=in_beginDate;
						latestStart[1]=counter;
					}
					
					if(in_endDate.before((Date) earliestFinish[0])) {
						System.out.println("this b date's end is before than earliest finish: "+String.valueOf(counter)+"-"+in_endDate);
						earliestFinish[0]=in_endDate;
						earliestFinish[1]=counter;
					}
					counter++;
				
					getDates.add(in_beginDate);
					getDates.add(in_endDate);
					}
				}//for loop
			
				Date checkFin = (Date) earliestFinish[0];
				Date checkStr = (Date) latestStart[0];
				
				System.out.println("range to compare: "+checkStr.toString()+"--"+checkFin.toString());
				//((Date) earliestFinish[0]).before((Date) latestStart[0])
				while (checkFin.before(checkStr)) {
					System.out.println("Oops problem in the system.. fixing now: "+String.valueOf(getDates.size()));
					
					int in2remove=getDates.indexOf(earliestFinish[1]);
					System.out.println("Removing: "+getDates.get(in2remove).toString());
					getDates.remove(in2remove);
					System.out.println("Removing: "+getDates.get(in2remove).toString());
					getDates.remove(in2remove);
					earliestFinish[0] = finDate;
					earliestFinish[1] = 0;
					
					//idk why
					System.out.println(isPartial);
					if(isPartial)
						earliestFinish[0] = augEnd;
					
					
					counter=0;
					for (int xx=0;xx<getDates.size();xx+=2) {
						if (getDates.get(xx).after((Date) latestStart[0])) {
							latestStart[0]=getDates.get(xx);
							latestStart[1]=counter;
							}
						
						if(getDates.get(xx+1).before((Date) earliestFinish[0])) {
							earliestFinish[0]=getDates.get(xx+1);
							earliestFinish[1]=counter;
							}
						counter++;
						
						}
					
					checkFin = (Date) earliestFinish[0];
					checkStr = (Date) latestStart[0];
				}//endwhile
				
				if(checkFin.compareTo(checkStr)==0) {
					cal.setTime(checkFin);
					cal.add(Calendar.DATE, 1);
					checkFin = cal.getTime();
				}
					
				//latestStart[1] = 0;
				//earliestFinish[1] = 0;
				int augClash=1;
				for (BookingDTO b: clashingBookingList) {
					//notforget self.
					if (b.getId_emp()!=emp_id) {//not sure if good idea v2
					Date in_beginDate = new SimpleDateFormat("dd/MM/yyyy").parse(b.getBegin_date());
					Date in_endDate = new SimpleDateFormat("dd/MM/yyyy").parse(b.getEnd_date());
					
					checkFin = (Date) earliestFinish[0];
					checkStr = (Date) latestStart[0];
					if(holidayDeductCalc(in_beginDate,in_endDate,checkStr,checkFin)>0)
						System.out.println("broken 40%s: "+in_beginDate.toString()+"-"+in_endDate.toString()+" x "+checkStr.toString()+"-"+checkFin.toString());
						augClash++;
					}
					
				}
				
				int maxAugClash=getMaxCanClashEmp(allEmpListPerDep,false);
				
				System.out.println("maxAugClash");
				System.out.println(maxAugClash);
				System.out.println(augClash);
				System.out.println(augClash>maxAugClash);
				if(augClash>maxAugClash)//+1
					condBreakerList[3]=1;
				
				return condBreakerList;
				//40 - 4clash, 60- 3clash tot 8.
				
			}//if for inside aug
			
		}catch(Exception e) {
			System.out.print("Fucked up");
			e.printStackTrace();
		}
			
		return condBreakerList;
	}
	
	//condbreaker array for headDep
	@Override
	public int[] holAllowanceForHeadDep(Date startDate,Date finDate, List<BookingDTO> listofAllDepBooking, EmployeeDTO thatEmployee,List<EmployeeDTO> allEmpListPerDep) {
		
		
		//-System.out.println("Welcome to the Land of C's!");
		int[] condBreakerList={0,0,0,0};
		
		List<EmployeeDTO> headDepList=new ArrayList<>();
		for (EmployeeDTO e : allEmpListPerDep) {
			if(e.getEmpRoleId()<2)
				headDepList.add(e);
		}
		
		int headDepCount=headDepList.size();
		
		//-System.out.println("headdepcount: "+String.valueOf(headDepCount));
		
		int maxCanClash=getMaxCanClashEmp(allEmpListPerDep,true);
		
		//-System.out.println("Max can clash C: "+String.valueOf(maxCanClash));
		
		List<BookingDTO> clashingBookingList = getClashingBookingList(startDate, finDate, thatEmployee, listofAllDepBooking);
		
		//startDate.getYear();
		
		/*Date date; // your date
		// Choose time zone in which you want to interpret your Date
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);*/
		
		
		
		/*
		 * Func C:
		 * get book list +
		 * create func with (sdate,edate,clashbooklist) +
		 * get latest start date, earliest end date incl main request +
		 * save both req for that +
		 * if late start is later than early finish
		 * ---e   s----
		 *   ---v--e
		 *       ----
		 *        ------      
		 * until its not the case, remove that breaker +
		 * put again.  
		 * 
		 *   
		 * else:
		 * -------
		 *   s----
		 *    -ve
		 * if date not lies in august, apply std procedure +
		 * if date lies in august, calc all and say it clashes 40% or not
		 * if date partial lies in aug, get 1 aug date or 31 aug date
		 * and apply std procedure
		 * 
		 * */
		
		/*
		 * Func F:
		 * recommendation for breaking 
		 * get random date, get available hol time
		 * add it to random s date. 
		 * check until no const. break
		 * ----
		 * change cond not breaker list for Func B:
		 * get the year, calc all booking that year (fake calc the ones 20/21)
		 * put them on new first list**** wait the other?
		 * change ispeak time for cumul peak time
		 * hard code peak time dates, apply datediffcalc
		 * add on request add peak time,
		 * may add another col to books, that cumul peak time update.
		 * give some # which wont work i guess. good night.
		 * 
		 * 
		 */
		System.out.println("size of clash booklist: "+String.valueOf(clashingBookingList.size()));
		
		
		boolean alreadyChecked=false;
		boolean isAug = checkIfinAug(startDate,finDate);

			
		if (isAug)
			condBreakerList=calcAugBreak(condBreakerList,startDate,finDate,allEmpListPerDep,clashingBookingList,thatEmployee.getId());

		if (condBreakerList[3]==0&&isAug)
			alreadyChecked=true;
		
		System.out.println("checked dates for const: "+startDate.toString()+"-"+finDate.toString());
		condBreakerList = calculateHolClashesAndConflicts(condBreakerList, clashingBookingList, thatEmployee, maxCanClash, headDepCount-1, 1);
		
			
		if (condBreakerList[3]==1&&alreadyChecked)
			condBreakerList[3]=0;
			
		
		return condBreakerList;

	}

	//condbreaker array for manager
	@Override
	public int[] holAllowanceForManSen(Date startDate,Date finDate, List<BookingDTO> listofAllDepBooking, EmployeeDTO thatEmployee,List<EmployeeDTO> allEmpListPerDep) {
		
		int[] condBreakerList={0,0,0,0};

		//mount to cond checker area.. getNofManagerAndSenior. then 60%.
		
		//get all manager/seniorlist count
		List<EmployeeDTO> manSerList=new ArrayList<>();
		for (EmployeeDTO e : allEmpListPerDep) {
			if(e.getEmpRoleId()==thatEmployee.getEmpRoleId())
				manSerList.add(e);
		}
		
		int manSerCount=manSerList.size();
		
		int maxCanClash=getMaxCanClashEmp(allEmpListPerDep,true);
		
		//-System.out.println("manser, max can clash");
		//-System.out.println(maxCanClash);
		//-System.out.println(manSerList.size());
		
		if(manSerList.size()==1) {
			condBreakerList[2]=1;
			System.out.println("cond2 broken!manser");
		}
		
		
		/* could be reopened to shortcut stuff.
		 * if (manSerCount==1) {// 1,2,3,4 cond checker returns maybe
			request.setAttribute("errorLoginMessage", "You are the only available Experienced staff, you cannot leave your people!");
			request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
		}*/
		
		
		List<BookingDTO> clashingBookingList = getClashingBookingList(startDate, finDate, thatEmployee, listofAllDepBooking);
		
		boolean alreadyChecked=false;
		boolean isAug = checkIfinAug(startDate,finDate);

		/*
		if (isAug)
			condBreakerList=calcAugBreak(condBreakerList,startDate,finDate,allEmpListPerDep,clashingBookingList,thatEmployee.getId());
	
		if (condBreakerList[3]==0&&isAug)
			alreadyChecked=true;*/
		System.out.println("checked dates for const: "+startDate.toString()+"-"+finDate.toString());
		condBreakerList = calculateHolClashesAndConflicts(condBreakerList, clashingBookingList, thatEmployee, maxCanClash, manSerCount, 2);
		
		
		if (condBreakerList[3]==1&&alreadyChecked)
			condBreakerList[3]=0;
			
		return condBreakerList;
		
	}
	
	@Override
	public int[] holAllowanceForNonSpecs(Date startDate,Date finDate, List<BookingDTO> listofAllDepBooking, EmployeeDTO thatEmployee,List<EmployeeDTO> allEmpListPerDep) {
		
		int[] condBreakerList={0,0,0,0};

		int maxCanClash=getMaxCanClashEmp(allEmpListPerDep,true);
		
		/* could be reopened to shortcut stuff.
		 * if (manSerCount==1) {// 1,2,3,4 cond checker returns maybe
			request.setAttribute("errorLoginMessage", "You are the only available Experienced staff, you cannot leave your people!");
			request.getRequestDispatcher("/newrequest.jsp").forward(request, response);
		}*/
		
		
		List<BookingDTO> clashingBookingList = getClashingBookingList(startDate, finDate, thatEmployee, listofAllDepBooking);
		
		boolean alreadyChecked=false;
		boolean isAug = checkIfinAug(startDate,finDate);

		/*
		if (isAug)
			condBreakerList=calcAugBreak(condBreakerList,startDate,finDate,allEmpListPerDep,clashingBookingList,thatEmployee.getId());
		
			
		if (condBreakerList[3]==0&&isAug)
			alreadyChecked=true;*/
		System.out.println("checked dates for const: "+startDate.toString()+"-"+finDate.toString());
		condBreakerList = calculateHolClashesAndConflicts(condBreakerList, clashingBookingList, thatEmployee, maxCanClash, -1, 3);

			
		if (condBreakerList[3]==1&&alreadyChecked)
			condBreakerList[3]=0;
		
		return condBreakerList;
		
	}
	
	public List<Object> getAllOutstandingRequestsByDep(int id_dep){
		
		List<RequestDTO> allRequestsByDep = holidayBookingAppBean.getAllRequestByDep(id_dep);
		List<BookingDTO> listofAllDepBooking = holidayBookingAppBean.getAllBookingsByDep(id_dep);//getAllBookingsperEmp(email);
		List<EmployeeDTO> allEmpListPerDep=holidayBookingAppBean.getAllEmployeesByDep(id_dep);
		
		List<RequestDTO> ruleBreakingReqsList = new ArrayList<>();
		List<RequestDTO> ruleNotBreakingReqsList = new ArrayList<>();
		List<int[]> brokenRulesList=new ArrayList<>();
		
		List<Object> allOutstandingReqs = new ArrayList<>();
		
		try {
			
			for (RequestDTO r : allRequestsByDep) {
				
				//-System.out.println("------------ New Request ----------");
				//-System.out.println(r.getId());
				
				if(r.getStatus()!=2)
					continue;
				
				int[] condBreakerList={0,0,0,0};
				boolean didHaveErrorMessage=false;
				
				EmployeeDTO thatEmployee = holidayBookingAppBean.getEmployeeById(r.getId_emp());
				
				int holAbsoluteDuration=r.getDuration();
				
				//-System.out.println("Duration got");
				
				if(holAbsoluteDuration==0) {
					System.out.println("0 dur area");
					ruleNotBreakingReqsList.add(r);
					continue;
				}
				//-System.out.println("pre-remaining area");
				
				int remaining = holidayRemainingCalc(listofAllDepBooking, r.getBegin_date(), r.getEnd_date(), thatEmployee);
				
				//-System.out.println("remaining area");
				/*
				 * 
				 * xmass and other stuff have already been calculated in getDuration();
				 * appropriate holidatRemainingCalc to getDuration, maybe change some funcs in below to up?
				 * so calc remaining later. if remaining goes negative, add const checker
				 * because if multiple same requests are sent, it will go negative in the update/accepts
				 * but if requested duration is 0, then it shouldnt break any rules anyway.
				 * maybe if absoluteDuration==0 // absoluteDuration>0 and remaining-absol < 0 
				 * 
				 * 
				 * */
				
				//SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy"); //yyyy-MM-dd
				//SimpleDateFormat converter = new SimpleDateFormat("dd/MM/yyyy"); //yyyy-MM-dd
				
				Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(r.getBegin_date());
				Date finDate = new SimpleDateFormat("dd/MM/yyyy").parse(r.getEnd_date());
				
				int intDiffDays = dateDiffCalc(finDate,startDate);
				
				int xMassDeductor=xMassCheck(r.getBegin_date(), r.getEnd_date());//depends on deducting duration too.
				int absoluteDuration=intDiffDays-xMassDeductor;
				
				remaining-=absoluteDuration;
				
				//-System.out.println("remaining recalced=");
				//-System.out.println(remaining);

				if(remaining<0) {
					didHaveErrorMessage=true;
					condBreakerList[0]=1;
				}
				
				if(thatEmployee.getEmpRoleId()<2) {
					
					//-System.out.println("land of 2's");
					
					int[] temp_condBreakerList = holAllowanceForHeadDep(startDate,finDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
					
			
					if(temp_condBreakerList[1]==1) {
						didHaveErrorMessage=true;
						condBreakerList[1]=1;
					}
						
					if (temp_condBreakerList[3]==1) {
						didHaveErrorMessage=true;
						condBreakerList[3]=1;
					}
				
					
				}else if(thatEmployee.getEmpRoleId()==2 || thatEmployee.getEmpRoleId()==5) {
					
					//-System.out.println("land of 5's");
					int[] temp_condBreakerList=holAllowanceForManSen(startDate,finDate, listofAllDepBooking, thatEmployee, allEmpListPerDep);
					
					if(temp_condBreakerList[2]==1) {
						didHaveErrorMessage=true;
						condBreakerList[2]=1;
					}
					
					if (temp_condBreakerList[3]==1) {
						didHaveErrorMessage=true;
						condBreakerList[3]=1;
					}
				}
			
				
				if(!didHaveErrorMessage) {
					//-System.out.println("no errs so no rule breakk");
					ruleNotBreakingReqsList.add(r);
				}else {
					//-System.out.println("rule breakk");
					ruleBreakingReqsList.add(r);
					brokenRulesList.add(condBreakerList);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		allOutstandingReqs.add(ruleBreakingReqsList);
		allOutstandingReqs.add(brokenRulesList);
		allOutstandingReqs.add(ruleNotBreakingReqsList);
		
		return allOutstandingReqs;
	}
	
	
	public List<List<EmployeeDTO>> workingFilters(String email,String beginRange, String endRange){
		List<List<EmployeeDTO>> allEmpsFilter = new ArrayList<>();
		List<EmployeeDTO> workFilter = new ArrayList<>();
		List<EmployeeDTO> holidayFilter = new ArrayList<>();
		
		EmployeeDTO thatEmployee = holidayBookingAppBean.getEmployeeByEmail(email);
		
		List<EmployeeDTO> empsByDep = holidayBookingAppBean.getAllEmployeesByDep(thatEmployee.getDepId());
		
		try {
			Date startRange = new SimpleDateFormat("yyyy-MM-dd").parse(beginRange);
			Date finRange = new SimpleDateFormat("yyyy-MM-dd").parse(endRange);
			
			for(EmployeeDTO e : empsByDep) {
				
				List<BookingDTO> booksByEmp = holidayBookingAppBean.getAllBookingsperEmp(e.getEmail());
				
				int potentialAbsence=0;
				for (BookingDTO b : booksByEmp) {
					Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(b.getBegin_date());
					Date finDate = new SimpleDateFormat("dd/MM/yyyy").parse(b.getEnd_date());
					
					potentialAbsence = holidayDeductCalc(startDate,finDate,startRange,finRange);//check if good +=
					
					if (potentialAbsence>0) {
						holidayFilter.add(e);
						break;
					}
	
				}
				
				if (potentialAbsence==0) {
					workFilter.add(e);
				}
				//check different year possibilities too.	
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		allEmpsFilter.add(workFilter);
		allEmpsFilter.add(holidayFilter);
		
		return allEmpsFilter;
		
	}
	
}
