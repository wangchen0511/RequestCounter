package requestCounter;

public interface IRequestCounter {
	
	public long getLastHour() throws InstantiationException, IllegalAccessException;
	
	public long getThisHour() throws InstantiationException, IllegalAccessException;

	public long getLastMin() throws InstantiationException, IllegalAccessException;
	
	public long getThisMin() throws InstantiationException, IllegalAccessException;
	
	public long getLastSec() throws InstantiationException, IllegalAccessException;
	
	public long getThisSec() throws InstantiationException, IllegalAccessException;
	
	public long getLastSeveralMins(int mins) throws InstantiationException, IllegalAccessException;
	
	public long getLastSeveralSecs(int secs) throws InstantiationException, IllegalAccessException;
	
	public long getLastSeveralHours(int hours) throws InstantiationException, IllegalAccessException;
	
	public long getLastDay() throws InstantiationException, IllegalAccessException;
	
	public long getLastSeveralDays(int days) throws InstantiationException, IllegalAccessException;

	public void addOneRequest() throws InstantiationException, IllegalAccessException;
}
