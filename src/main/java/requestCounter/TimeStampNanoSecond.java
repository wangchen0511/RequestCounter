package requestCounter;

import java.util.concurrent.TimeUnit;


/**
 * 
 * @author adam701
 *
 *
 *	Immutable class.
 *
 *	This also has a relative start time. The start time is the tie JVM load the Class.
 */

public class TimeStampNanoSecond implements ITimeStamp {

	private long timeStamp;
	private static final long startTime = System.nanoTime();
	
	public TimeStampNanoSecond(){
		timeStamp = System.nanoTime();
	}
	
	@Override
	public long secDiff(ITimeStamp t1) {
		return TimeUnit.NANOSECONDS.toSeconds(timeStamp - startTime) - TimeUnit.NANOSECONDS.toSeconds(((TimeStampNanoSecond) t1).timeStamp - startTime);
	}

	@Override
	public long minDiff(ITimeStamp t1) {
		return TimeUnit.NANOSECONDS.toMinutes(timeStamp - startTime) - TimeUnit.NANOSECONDS.toMinutes(((TimeStampNanoSecond) t1).timeStamp - startTime);
	}

	@Override
	public long hourDiff(ITimeStamp t1) {
		return TimeUnit.NANOSECONDS.toHours(timeStamp - startTime) - TimeUnit.NANOSECONDS.toHours(((TimeStampNanoSecond) t1).timeStamp - startTime);
	}

	@Override
	public long dayDiff(ITimeStamp t1) {
		return TimeUnit.NANOSECONDS.toDays(timeStamp - startTime) - TimeUnit.NANOSECONDS.toDays(((TimeStampNanoSecond) t1).timeStamp - startTime);
	}

	@Override
	public boolean isSameSec(ITimeStamp t1) {
		return secDiff(t1) == 0;
	}

	@Override
	public boolean isSameMin(ITimeStamp t1) {
		return minDiff(t1) == 0;
	}

	@Override
	public boolean isSameHour(ITimeStamp t1) {
		return hourDiff(t1) == 0;
	}

	@Override
	public boolean isSameDay(ITimeStamp t1) {
		return dayDiff(t1) == 0;
	}
}
