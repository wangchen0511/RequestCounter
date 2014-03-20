package requestCounter;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author adam701
 *
 *
 *	Immutable class.
 *
 *	This is time stamp is used for real world time. That means when we say this hour, 
 *	if current time is 8:15, then this hour means from 8:00 ~ 9:00. Last hour means 7:00 ~ 8:00.
 */

public class TimeStampCalendar implements ITimeStamp {
	private final Calendar cal;
	private final Calendar calSec;
	private final Calendar calMin;
	private final Calendar calHour;
	private final Calendar calDay;
	
	public TimeStampCalendar(Calendar cal){
		this.cal = cal;
		this.calSec = generateSecStamp(cal);
		this.calMin = generateMinStamp(cal);
		this.calHour = generateHourStamp(cal);
		this.calDay = generateDayStamp(cal);
	}
	
	public TimeStampCalendar(){
		this(Calendar.getInstance());
	}

	private Calendar generateSecStamp(final Calendar secStamp){
		Calendar ret = (Calendar) secStamp.clone();
		ret.set(Calendar.MILLISECOND, 0);
		return ret;
	}
	
	private Calendar generateMinStamp(final Calendar minStamp){
		Calendar ret = generateSecStamp(minStamp);
		ret.set(Calendar.SECOND, 0);
		return ret;
	}
	
	private Calendar generateHourStamp(final Calendar hourStamp){
		Calendar ret = generateMinStamp(hourStamp);
		ret.set(Calendar.MINUTE, 0);
		return ret;
	}
	
	private Calendar generateDayStamp(final Calendar dayStamp){
		Calendar ret = generateHourStamp(dayStamp);
		ret.set(Calendar.HOUR_OF_DAY, 0);
		return ret;
	}
	
	@Override
	public long secDiff(ITimeStamp t1) {
		TimeStampCalendar input = (TimeStampCalendar) t1;
		return TimeUnit.MILLISECONDS.toSeconds(this.calSec.getTimeInMillis() - input.calSec.getTimeInMillis());
	}

	@Override
	public long minDiff(ITimeStamp t1) {
		TimeStampCalendar input = (TimeStampCalendar) t1;
		return TimeUnit.MILLISECONDS.toMinutes(this.calMin.getTimeInMillis() - input.calMin.getTimeInMillis());
	}

	@Override
	public long hourDiff(ITimeStamp t1) {
		TimeStampCalendar input = (TimeStampCalendar) t1;
		return TimeUnit.MILLISECONDS.toHours(this.calHour.getTimeInMillis() - input.calHour.getTimeInMillis());
	}

	@Override
	public long dayDiff(ITimeStamp t1) {
		TimeStampCalendar input = (TimeStampCalendar) t1;
		return TimeUnit.MILLISECONDS.toDays(this.calDay.getTimeInMillis() - input.calDay.getTimeInMillis());
	}

	@Override
	public boolean isSameSec(ITimeStamp t1) {
		if(secDiff(t1) == 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean isSameMin(ITimeStamp t1) {
		if(minDiff(t1) == 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean isSameHour(ITimeStamp t1) {
		if(hourDiff(t1) == 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean isSameDay(ITimeStamp t1) {
		if(dayDiff(t1) == 0){
			return true;
		}
		return false;
	}
}
