package requestCounter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups="unit")
public class RequestCounterLowAccuracyTest {
	
	@Test(description="Test get This Hour")
	public void testGetThisHour() throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, InterruptedException{
		final RequestCounterLowAccuracy instance = new RequestCounterLowAccuracy(TimeStampCalendar.class);
		
		ScheduledExecutorService exec = Executors.newScheduledThreadPool(100);
		
		class Task implements Runnable{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					instance.addOneRequest();
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for(int i =0; i < 300; i++){
			exec.scheduleAtFixedRate(new Task(), 200, 200, TimeUnit.MILLISECONDS);
		}

		
		for(int i = 0; i < 300; i++){
			Thread.sleep(2000);
			System.out.println("Last Min" + instance.getLastMin());
			System.out.println("This Min" + instance.getThisMin());
			System.out.println("Last Sec" + instance.getLastSec());
			System.out.println("This Sec" + instance.getThisSec());
			System.out.println("Last Hour" + instance.getLastHour());
			System.out.println("This Hour" + instance.getThisHour());
		}
		exec.shutdown();
		//Assert.assertEquals(instance.getLastHour(), 0);
		//Assert.assertEquals(instance.getThisHour(), 100);
	}
	
}
