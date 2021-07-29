package com.sol.work;

import com.sol.store.TickStore;
import com.sol.utils.Constants;
import com.sol.vo.Tick;

public class TickWorker implements Runnable {

	private final Tick tick;

	public TickWorker(Tick tick) {
		this.tick = tick;
	}

	@Override
	public void run() {
		addForAllWithUniqueTick(tick.getTimestamp(), tick.getPrice());
		addForSingleInstrument(tick);
	}

	private void addForSingleInstrument(Tick tick) {
		String instrument = tick.getInstrument();
		TickStore.getInstance().getInstrumentsWindows().compute(instrument,
				(k, v) -> v == null ? new SlidingWindow().addToQueue(tick) : v.addToQueue(tick));
	}

	/*
	 * Takes care of collision, and always generates a unique key for map - This is
	 * only for all ticks, for individual instrument this is not possible(assumption
	 * - per second tick generation, but could be implemented if generation is fast)
	 */
	private void addForAllWithUniqueTick(long timestamp, double price) {
		long index = timestamp * Constants.COLLISION_SIZE;
		long maxblock = index + Constants.COLLISION_SIZE - 1;
		while (true) {
			Double val = TickStore.getInstance().getAllTickPrices().putIfAbsent(index, price);
			if (val == null || index > maxblock)
				break;
			else {
				index = index + 1;
			}
		}
	}

}
