package requestCounter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

public class RequestCounterLowAccurayNanoTimeTest {

	@Test(description="Test get This Hour")
	public void testGetThisHour() throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, InterruptedException{
		final RequestCounterLowAccuracy instance = new RequestCounterLowAccuracy(TimeStampNanoSecond.class);//
		
		//ScheduledExecutorService exec = Executors.newScheduledThreadPool(100);
		System.out.println("For our test! ");
		ExecutorService exec = Executors.newCachedThreadPool();
		final CountDownLatch startLatch = new CountDownLatch(101);
		final CountDownLatch doneLatch = new CountDownLatch(101);
		long startTime, endTime;
		class Task implements Runnable{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					startLatch.countDown();
					startLatch.await();
					for(int i = 0; i < 10000000; i++){
						instance.addOneRequest();
					}
					doneLatch.countDown();
				} catch (InstantiationException | IllegalAccessException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for(int i = 0; i < 100; i++){
			exec.execute(new Task());
		}
		startLatch.countDown();
		startTime = System.nanoTime();
		doneLatch.countDown();
		doneLatch.await();
		endTime = System.nanoTime();
		
		System.out.println("Total time " + TimeUnit.NANOSECONDS.toSeconds(endTime - startTime));
		System.out.println("Last Sec " + instance.getLastSec());
		System.out.println("This Min " + instance.getThisMin());
		System.out.println("This Hour " + instance.getThisHour());
		
		exec.shutdown();
	}
	
}
