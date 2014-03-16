package requestCounter;

/**
 * 
 * @author adam701
 *
 *	Should be immutable
 *
 */

public interface ITimeStamp {
	public long secDiff(ITimeStamp t1);//It will be positve if current timeStamp is later than t1.
	
	public long minDiff(ITimeStamp t1);
	
	public long hourDiff(ITimeStamp t1);
	
	public long dayDiff(ITimeStamp t1);
	
	public boolean isSameSec(ITimeStamp t1);
	
	public boolean isSameMin(ITimeStamp t1);
	
	public boolean isSameHour(ITimeStamp t1);
	
	public boolean isSameDay(ITimeStamp t1);
}
