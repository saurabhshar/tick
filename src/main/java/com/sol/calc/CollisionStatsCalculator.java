package com.sol.calc;

import java.util.concurrent.ConcurrentSkipListMap;

import com.sol.store.TickStore;
import com.sol.utils.Constants;

public class CollisionStatsCalculator extends StatsCalculator {

	@Override
	public long getStartTime() {
		return (System.currentTimeMillis() - Constants.TICK_LIFE) * Constants.COLLISION_SIZE;
	}

	@Override
	public void run() {
		calculate(null, null);
	}
	
	@Override
	public ConcurrentSkipListMap<Long, Double> getTickMap(ConcurrentSkipListMap<Long, Double> tickMap) {
		return TickStore.getInstance().getAllTickPrices();
	}
}
