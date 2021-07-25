package com.sol.work;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.sol.utils.Constants;
import com.sol.vo.Tick;

public class SlidingWindowTest {

	@Test
	public void testOrdering() {
		SlidingWindow sw = new SlidingWindow();
		long now = System.currentTimeMillis();
		Tick t1 = new Tick();
		t1.setTimestamp(now);
		t1.setInstrument("one");
		sw.addToQueue(t1);

		Tick t2 = new Tick();
		t2.setTimestamp(now - 100);
		sw.addToQueue(t2);
		t2.setInstrument("two");

		Tick t3 = new Tick();
		t3.setTimestamp(now - 200);
		sw.addToQueue(t3);
		t3.setInstrument("three");

		Tick t4 = new Tick();
		t4.setTimestamp(now + 300);
		sw.addToQueue(t4);
		t4.setInstrument("four");

		Tick t5 = new Tick();
		t5.setTimestamp(now - Constants.TICK_LIFE);
		sw.addToQueue(t5);
		t5.setInstrument("five");

		// assertTrue(sw.tickWindow.size() == 4);
		// assertTrue(sw.getAllTicksInWindow().firstKey() == now - 200);

		Tick t6 = new Tick();
		t6.setTimestamp(now - Constants.TICK_LIFE + 50);
		sw.addToQueue(t6);
		t6.setInstrument("six");
		// assertTrue(sw.tickWindow.size() == 5);

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		// assertTrue(sw.tickWindow.size() == 5);
		
		Tick t7 = new Tick();
		t7.setTimestamp(now+10);
		sw.addToQueue(t7);
		t7.setInstrument("seven");
		
		// assertTrue(sw.tickWindow.size() == 5);//3274
		
		// System.out.println(sw.tickWindow);
		System.out.println(sw.getStats());
	}
}
