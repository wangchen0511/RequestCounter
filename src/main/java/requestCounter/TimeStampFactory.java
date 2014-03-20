package requestCounter;



public class TimeStampFactory {
	public static ITimeStamp getTimeStamp(Class<? extends ITimeStamp> timeStampType){
		if(timeStampType == TimeStampNanoSecond.class){
			return new TimeStampNanoSecond();
		}else if(timeStampType == TimeStampCalendar.class){
			return new TimeStampCalendar();
		}else{
			throw new RuntimeException("Wrong time stamp type!");
		}
	}
}
