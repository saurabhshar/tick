package com.sol.work;

import java.util.DoubleSummaryStatistics;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.sol.utils.Constants;
import com.sol.vo.Stats;
import com.sol.vo.Tick;

public class SlidingWindow {

	ConcurrentSkipListMap<Long, Tick> tickWindow = new ConcurrentSkipListMap<>();

	private Stats stats;

	public SlidingWindow() {
	}

	public SlidingWindow addToQueue(Tick tick) {

		if (liveableTick(tick)) {
			tickWindow.put(tick.getTimestamp(), tick);
		}
		stats = reCalculateStats();
		// *** should we throw 204 otherwise?
		return this;
	}

	ConcurrentNavigableMap<Long, Tick> getAllTicksInWindow() {
		long start = System.currentTimeMillis() - Constants.TICK_LIFE;
		return tickWindow.tailMap(start);
	}

	private boolean liveableTick(Tick tick) {
		return tick.getTimestamp() > (System.currentTimeMillis() - Constants.TICK_LIFE);
	}

	private Stats reCalculateStats() {
		// Move this method to a strategy class ****
		ConcurrentNavigableMap<Long, Tick> allTicksInCurrentWindow = getAllTicksInWindow();
		DoubleSummaryStatistics statistics = allTicksInCurrentWindow.values().stream().mapToDouble(n -> n.getPrice())
				.summaryStatistics();
		Stats currentStats = new Stats(statistics.getAverage(), statistics.getMax(), statistics.getMin(),
				statistics.getCount());
		// **** thousands of objects being recreated every second!!!!!!!
		clear();
		return currentStats;
	}

	public Stats getStats() {
		if (stats == null) {
			throw new RuntimeException("Not enough ticks received to generate stats yet!!");
		}
		return stats;
	}

	private void clear() {
		long windowStartTime = System.currentTimeMillis() - Constants.TICK_LIFE;
		tickWindow.headMap(windowStartTime).clear();
	}
}