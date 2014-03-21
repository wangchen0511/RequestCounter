package requestCounter;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class RequestCounterThreadBased implements IRequestCounter {

	private final AtomicInteger secCounter = new AtomicInteger(0);
	private final AtomicInteger minCounter = new AtomicInteger(0);
	private final AtomicLong currentSecReqs = new AtomicLong(0);
	private final AtomicLong currentMinReqs = new AtomicLong(0);
	private final AtomicLong currentHourReqs = new AtomicLong(0);
	private final ICircularBuffer<Long> secQueue = new ArrayCircularBuffer<Long>(60);
	private final ICircularBuffer<Long> minQueue = new ArrayCircularBuffer<Long>(60);
	private final ICircularBuffer<Long> hourQueue = new ArrayCircularBuffer<Long>(72);
	private final ReentrantLock lock = new ReentrantLock();
	private final ScheduledExecutorService exec = Executors.newScheduledThreadPool(5);
	
	private class SecTask implements Runnable{

		@Override
		public void run() {
			try{
				lock.lock();
				secQueue.add(currentSecReqs.getAndSet(0));
				if(secCounter.incrementAndGet() == 60){
					secCounter.set(0);
					minQueue.add(currentMinReqs.getAndSet(0));
					if(minCounter.incrementAndGet() == 60){
						minCounter.set(0);
						hourQueue.add(currentHourReqs.getAndSet(0));
					}
				}
			}finally{
				lock.unlock();
			}	
		}
		
	}
	
	public RequestCounterThreadBased(){
		exec.scheduleAtFixedRate(new SecTask(), 0, 1000, TimeUnit.MILLISECONDS);
	}
	
	
	
	@Override
	public long getLastHour() throws InstantiationException,
			IllegalAccessException {
		return getLastSeveralHours(1);
	}

	@Override
	public long getThisHour() throws InstantiationException,
			IllegalAccessException {
		return currentHourReqs.get();
	}

	@Override
	public long getLastMin() throws InstantiationException,
			IllegalAccessException {
		return getLastSeveralHours(1);
	}

	@Override
	public long getThisMin() throws InstantiationException,
			IllegalAccessException {
		return currentMinReqs.get();
	}

	@Override
	public long getLastSec() throws InstantiationException,
			IllegalAccessException {
		return getLastSeveralSecs(1);
	}

	@Override
	public long getThisSec() throws InstantiationException,
			IllegalAccessException {
		return currentSecReqs.get();
	}

	@Override
	public long getLastSeveralMins(int mins) throws InstantiationException,
			IllegalAccessException {
		Iterator<Long> minIter = minQueue.iteratorFromTailToHead();
		long res = 0;
		for(int i = 0; i < mins; i++){
			if(!minIter.hasNext()){
				break;
			}
			res += minIter.next();
		}
		return res;
	}
	
	public void addOneRequest(){
		currentSecReqs.incrementAndGet();
		currentMinReqs.incrementAndGet();
		currentHourReqs.incrementAndGet();
	}
	

	@Override
	public long getLastSeveralSecs(int secs) throws InstantiationException,
			IllegalAccessException {
		Iterator<Long> secIter = secQueue.iteratorFromTailToHead();
		long res = 0;
		for(int i = 0; i < secs; i++){
			if(!secIter.hasNext()){
				break;
			}
			res += secIter.next();
		}
		return res;
	}

	@Override
	public long getLastSeveralHours(int hours) throws InstantiationException,
			IllegalAccessException {
		Iterator<Long> hourIter = minQueue.iteratorFromTailToHead();
		long res = 0;
		for(int i = 0; i < hours; i++){
			if(!hourIter.hasNext()){
				break;
			}
			res += hourIter.next();
		}
		return res;
	}

	@Override
	public long getLastDay() throws InstantiationException,
			IllegalAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLastSeveralDays(int days) throws InstantiationException,
			IllegalAccessException {
		// TODO Auto-generated method stub
		return 0;
	}

}
