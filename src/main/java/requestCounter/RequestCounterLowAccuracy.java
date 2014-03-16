package requestCounter;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;


public class RequestCounterLowAccuracy implements IRequestCounter{
	
	static class TimeElement{
		private final ITimeStamp startTimeStamp;
		private final AtomicLong requestNumOneSec;
		
		public TimeElement(ITimeStamp startTime, int num){
			this.startTimeStamp = startTime;
			this.requestNumOneSec = new AtomicLong(num);
		}
		
		public void addRequests(long num){
			requestNumOneSec.addAndGet(num);
		}
		
		public ITimeStamp getStartTime(){
			return this.startTimeStamp;
		}
		
		public long getRequestNum(){
			return this.requestNumOneSec.get();
		}
	}
	
	private final ICircularBuffer<TimeElement> secBuffer = new CircularBuffer<TimeElement>(60);
	private final ICircularBuffer<TimeElement> minBuffer = new CircularBuffer<TimeElement>(60);
	private final ICircularBuffer<TimeElement> hourBuffer = new CircularBuffer<TimeElement>(24);
	private final ICircularBuffer<TimeElement> dayBuffer = new CircularBuffer<TimeElement>(180);
	private TimeElement currentSec;
	private TimeElement currentMin;
	private TimeElement currentHour;
	private TimeElement currentDay;
	private final Class<? extends ITimeStamp> timeStampClass;
	
	public static enum BufferType{
		SEC, MIN, HOUR, DAY;
	}
	
	public RequestCounterLowAccuracy(Class<? extends ITimeStamp> timeStampClass) throws InstantiationException, IllegalAccessException{
		this.timeStampClass = timeStampClass;
		ITimeStamp timeStamp = this.timeStampClass.newInstance();
		this.currentSec = new TimeElement(timeStamp, 0);
		this.currentMin = new TimeElement(timeStamp, 0);
		this.currentHour = new TimeElement(timeStamp, 0);
		this.currentDay = new TimeElement(timeStamp, 0);
	}
	
	public void addOneRequest() throws InstantiationException, IllegalAccessException{
		ITimeStamp currentTime = this.timeStampClass.newInstance();
		addNumToDayBuffer(1, currentTime);//Run fast task firstly
		addNumToHourBuffer(1, currentTime);
		addNumToMinBuffer(1, currentTime);
		addNumToSecBuffer(1, currentTime);
	}
	
	
	//It is possible that currentTime < currentSec
	private void updateSecBuffer(ITimeStamp currentTime){
		if(currentTime.secDiff(currentSec.getStartTime()) > 0){
			secBuffer.add(currentSec);
			currentSec = new TimeElement(currentTime, 0);
		}
	}
	
	private void addNumToSecBuffer(long num, ITimeStamp currentTime){
		synchronized(secBuffer){
			updateSecBuffer(currentTime);
			currentSec.addRequests(num);
		}
	}
	
	
	private void updateMinBuffer(ITimeStamp currentTime){
		if(currentTime.minDiff(currentMin.getStartTime()) > 0){
			minBuffer.add(currentMin);
			currentMin = new TimeElement(currentTime, 0);
		}
	}
	
	private void addNumToMinBuffer(long num, ITimeStamp currentTime){
		synchronized(minBuffer){
			updateMinBuffer(currentTime);
			currentMin.addRequests(num);
		}
	}
	
	private void updateHourBuffer(ITimeStamp currentTime){
		if(currentTime.hourDiff(currentHour.getStartTime()) > 0){
			hourBuffer.add(currentHour);
			currentHour = new TimeElement(currentTime, 0);
		}
	}
	
	private void addNumToHourBuffer(long num, ITimeStamp currentTime){
		synchronized(hourBuffer){
			updateHourBuffer(currentTime);
			currentHour.addRequests(num);
		}
	}
	
	private void updateDayBuffer(ITimeStamp currentTime){
		if(currentTime.dayDiff(currentDay.getStartTime()) > 0){
			dayBuffer.add(currentDay);
			currentDay = new TimeElement(currentTime, 0);
		}
	}
	
	private void addNumToDayBuffer(long num, ITimeStamp currentTime){
		synchronized(dayBuffer){
			updateDayBuffer(currentTime);
			currentDay.addRequests(num);
		}
	}
	
	private long requestStat(ITimeStamp currentTime, int offSet, Iterator<TimeElement> iter, BufferType type){
		long res = 0;
		while(iter.hasNext()){
			TimeElement element = iter.next();
			long currentOffSet;
			switch(type){
				case SEC:
					currentOffSet = currentTime.secDiff(element.getStartTime());
					break;
				case MIN:
					currentOffSet = currentTime.minDiff(element.getStartTime());
					break;
				case HOUR:
					currentOffSet = currentTime.hourDiff(element.getStartTime());
					break;
				case DAY:
					currentOffSet = currentTime.dayDiff(element.getStartTime());
					break;
				default:
					throw new RuntimeException("Invalid Buffer Type");
			}
			if(currentOffSet <= offSet){
				res += element.getRequestNum();
			}else{
				break;
			}
		}
		return res;
	}

	public long getLastMin() throws InstantiationException, IllegalAccessException {
		return getLastSeveralMins(1);
	}

	public long getLastSec() throws InstantiationException, IllegalAccessException {
		return getLastSeveralSecs(1);
	}

	public long getThisSec() throws InstantiationException, IllegalAccessException {
		synchronized(secBuffer){
			ITimeStamp currentTime = this.timeStampClass.newInstance();
			updateSecBuffer(currentTime);
			return currentSec.getRequestNum();
		}
	}

	public long getLastSeveralMins(int mins) throws InstantiationException, IllegalAccessException {
		synchronized(minBuffer){
			ITimeStamp currentTime = this.timeStampClass.newInstance();
			updateMinBuffer(currentTime);
			Iterator<TimeElement> iter = minBuffer.iteratorFromTailToHead();
			return requestStat(currentTime, mins, iter, BufferType.MIN);
		}
	}

	public long getLastSeveralSecs(int secs) throws InstantiationException, IllegalAccessException {
		synchronized(secBuffer){
			ITimeStamp currentTime = this.timeStampClass.newInstance();
			updateSecBuffer(currentTime);
			Iterator<TimeElement> iter = secBuffer.iteratorFromTailToHead();
			return requestStat(currentTime, secs, iter, BufferType.SEC);
		}
	}

	public long getLastHour() throws InstantiationException, IllegalAccessException {
		return getLastSeveralHours(1);
	}

	public long getThisHour() throws InstantiationException, IllegalAccessException {
		synchronized(hourBuffer){
			ITimeStamp currentTime = this.timeStampClass.newInstance();
			updateHourBuffer(currentTime);
			return currentHour.getRequestNum();
		}
	}

	public long getThisMin() throws InstantiationException, IllegalAccessException {
		synchronized(minBuffer){
			ITimeStamp currentTime = this.timeStampClass.newInstance();
			updateSecBuffer(currentTime);
			return currentMin.getRequestNum();
		}
	}

	@Override
	public long getLastSeveralHours(int hours) throws InstantiationException, IllegalAccessException {
		synchronized(hourBuffer){
			ITimeStamp currentTime = this.timeStampClass.newInstance();
			updateHourBuffer(currentTime);
			Iterator<TimeElement> iter = hourBuffer.iteratorFromTailToHead();
			return requestStat(currentTime, hours, iter, BufferType.HOUR);
		}
	}

	@Override
	public long getLastDay() throws InstantiationException,
			IllegalAccessException {
		return getLastSeveralDays(1);
	}

	@Override
	public long getLastSeveralDays(int days) throws InstantiationException,
			IllegalAccessException {
		synchronized(dayBuffer){
			ITimeStamp currentTime = this.timeStampClass.newInstance();
			updateDayBuffer(currentTime);
			Iterator<TimeElement> iter = dayBuffer.iteratorFromTailToHead();
			return requestStat(currentTime, days, iter, BufferType.DAY);
		}
	}
	
}
