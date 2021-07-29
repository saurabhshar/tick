package com.sol.calc;

import java.util.DoubleSummaryStatistics;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicReference;

import com.sol.store.TickStore;
import com.sol.utils.Constants;
import com.sol.vo.Stats;

public class StatsCalculator implements StatsCalc {

	public void calculate(ConcurrentSkipListMap<Long, Double> tMap, AtomicReference<Stats> stats) {
		long start = getStartTime();
		ConcurrentSkipListMap<Long, Double> tickMap = getTickMap(tMap);
		clearWindow(tickMap, start);
		Stats localStats = calculateForSnapshot(start, tickMap);
		stats = (stats != null) ? stats : TickStore.getInstance().getAllStatsRef();
		stats.set(localStats);
	}

	public long getStartTime() {
		return System.currentTimeMillis() - Constants.TICK_LIFE;
	}

	@Override
	public void run() {
		/*
		 * Doesn't need to run anything here - happens for all ticks only.
		 */
	}

	private void clearWindow(ConcurrentSkipListMap<Long, Double> tickMap, long start) {
		tickMap.headMap(start).clear();
	}

	public ConcurrentSkipListMap<Long, Double> getTickMap(ConcurrentSkipListMap<Long, Double> tickMap) {
		return tickMap;
	}

	ConcurrentNavigableMap<Long, Double> snapshotWindow(ConcurrentSkipListMap<Long, Double> tickMap, long start) {
		ConcurrentNavigableMap<Long, Double> map = tickMap.tailMap(start);
		return (map != null ? map : new ConcurrentSkipListMap<Long, Double>());
	}

	public Stats calculateForSnapshot(long start, ConcurrentSkipListMap<Long, Double> tickMap) {
		ConcurrentNavigableMap<Long, Double> allTicksInCurrentWindow = snapshotWindow(tickMap, start);
		DoubleSummaryStatistics statistics = allTicksInCurrentWindow.values().stream().mapToDouble(n -> n)
				.summaryStatistics();
		return new Stats(statistics.getAverage(), statistics.getMax(), statistics.getMin(), statistics.getCount());
	}

}
