package com.cse694;

public class CheckInWait {
	
    public void waitForSeat() {
        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}