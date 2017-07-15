package java.util;

/**
 * Class designed to manage Date and Time.
 * 
 * Note: some methods are deprecated.
 * 
 * @author Juan Antonio Brenha Moral
 */
/*
 * TODO: DEVELOPER NOTES: Since 1.1 Sun has used Calendar for the setXXX() methods and has
 * removed them from the Date class. I'm not sure why Juan chose to implement an outdated
 * class here rather than the newer ones. -BB 
 */
public class Date {
	// TODO: These values seem kind of arbitrary
	private int year = 2000;
	private int month = 1;
	private int day = 1;

	private int hours = 0;
	private int minutes = 0;
	private int seconds = 0;

	public Date(){
		//Empty
	}
	
	//TODO Juan didn't override toString()
	
	/*
	 * GETTERS & SETTERS
	 */
	
	/**
	 * Set Year
	 */
	public void setYear(int yyyy){ 
		if((yyyy >=0) && (yyyy <= 99)){
			year = 2000 + yyyy;
		}
	}
	
	/**
	 * Get year
	 * 
	 * @return the year
	 */
	public int getYear(){
		return year;
	}

	/**
	 * Set Month
	 * 
	 * @param mm the month
	 */
	public void setMonth(int mm){
		if((mm >= 1) && (mm<=12)){
			month = mm;
		}
	}
	
	/**
	 * Get Month
	 * 
	 * @return the month
	 */
	public int getMonth(){
		return month;
	}
	
	/**
	 * Set Day
	 * 
	 * @param dd the day
	 */
	public void setDate(int dd){
		if((dd>=1) && (dd<=31)){
			day = dd;
		}
	}

	/**
	 * Get Day
	 * 
	 * @return the day
	 */
	public int getDate(){
		//TODO this should return the day of week instead of the day of the month
		return day;
	}

	/**
	 * Set hours
	 * 
	 * @param hh the hours
	 */
	public void setHours(int hh){
		if((hh >= 0) && (hh<= 23)){
			hours = hh;
		}
	}

	/**
	 * Get Hours
	 * 
	 * @return the hours
	 */
	public int getHours(){
		return hours;
	}

	/**
	 * Set Minutes
	 * 
	 * @param mm the minutes
	 */
	public void setMinutes(int mm){
		if((mm >= 0) && (mm <= 59)){
			minutes = mm;
		}
	}
	
	/**
	 * Get Minutes
	 * 
	 * @return the minutes
	 */
	public int getMinutes(){
		return minutes;
	}
	
	/**
	 * Set Seconds
	 * 
	 * @param ss the seconds
	 */
	public void setSeconds(int ss){
		if((ss >= 0) && (ss <= 59)){
			seconds = ss;
		}
	}

	/**
	 * Get Seconds
	 * 
	 * @return the seconds
	 */
	public int getSeconds(){
		return seconds;
	}

	/*
	 * UTILS
	 */
	
	/**
	 * Compare 2 Date objects to know if current Date object is before 
	 * than parameter
	 * 
	 * @param when the date to compare with
	 */
	public boolean before(Date when) {
		return getMillisOf(this) < getMillisOf(when);
	}

	/**
	 * Compare 2 Date objects to know if current Date object is after 
	 * than parameter
	 * 
	 * @param when the date to compare with
	 */
	public boolean after(Date when) {
		return getMillisOf(this) > getMillisOf(when);
	}

	/**
	 * TODO this method is broken.
	 * Returns the millisecond value of this <code>Date</code> object
	 * without affecting its internal state.
	 * return the amount of time in milliseconds from 1/1/1970
	 */
	private int getMillisOf(Date date){
		return 1;
	}
}
