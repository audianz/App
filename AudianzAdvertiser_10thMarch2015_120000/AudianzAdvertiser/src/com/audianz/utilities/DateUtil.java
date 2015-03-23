package com.audianz.utilities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utility class for date manipulation.
 * This class gives a simple interface for common Date, Calendar and Timezone
 * operations.
 * It is possible to apply subsequent transformations to an initial date, and
 * retrieve the changed Date object at any point.
 *
 */
public class DateUtil {
    
    //-------------------------------------------------------------- Attributes
    private Calendar cal;
    
    //------------------------------------------------------------ Constructors
    
    /** Inizialize a new instance with the current date */
    public DateUtil() {
        this(new Date());
    }
    
    /** Inizialize a new instance with the given date */
    public DateUtil(Date d) {
        cal = Calendar.getInstance();
        cal.setTime(d);
      
    }
    
    //---------------------------------------------------------- Public methods
    
    /** Set a new time */
    public void setTime(Date d) {
        cal.setTime(d);
    }
    
    /** Get the current time */
    public Date getTime() {
        return cal.getTime();
    }
    
    /** Get the current TimeZone */
    public String getTZ() {
        return cal.getTimeZone().getID();
    }
    
    /**
     * Convert the time to the midnight of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil toMidnight() {
        
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * Convert the time to the midnight of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil toEndOfDay() {
        
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * This method is used to return current time stamp
     * @return
     */
    public static Timestamp getCurrentTimeStamp()
    {
    	Date dt = new Date();
    	return (new Timestamp(dt.getTime()));
    }
    /**
     * Convert the current time to the start of week time of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil startOfWeek() {
    	cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * Convert the current time to the end of week time of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil endOfWeek() {
    	cal.set(Calendar.DAY_OF_WEEK, 7);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * Convert the current time to the start of week time of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil startOfMonth() {
    	cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * Convert the current time to the end of week time of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil endOfMonth(int lastDayOfMonth) {
    	cal.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * This method takes input long date value and date format type, and converts the date into given date format.  
     * @param date
     * @param formatType
     * @return
     */
    public String myDateFormatter(long date , String formatType){
    	String dateString = "";
    	try {
		  // Create Date object.
    	  Date d = new Date(date);
    	  //Create object of SimpleDateFormat and pass the desired date format.
    	  SimpleDateFormat sdf = new SimpleDateFormat(formatType);
    	  dateString = sdf.format(d);
    	} catch (Exception e) {
			// TODO: handle exception
		}
    	return dateString;
    }
    public String usingDateFormatter(long input){
        Date date = new Date(input);
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/dd hh:mm:ss z");
        sdf.setCalendar(cal);
        cal.setTime(date);
        return sdf.format(date);
 
    }
}
