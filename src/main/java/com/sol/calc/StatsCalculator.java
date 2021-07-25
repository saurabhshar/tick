package com.sol.calc;

import java.util.DoubleSummaryStatistics;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicReference;

import com.sol.utils.Constants;
import com.sol.vo.Stats;

public class StatsCalculator implements StatsCalc {

	private final ConcurrentSkipListMap<Long, Double> tsPriceMap;
	private final AtomicReference<Stats> statsRef;
	private final int collisionSize;

	public StatsCalculator(ConcurrentSkipListMap<Long, Double> tsPriceMap, AtomicReference<Stats> statsRef,
			int collisionSize) {
		this.tsPriceMap = tsPriceMap;
		this.statsRef = statsRef;
		this.collisionSize = collisionSize;
	}

	@Override
	public void calculate() {
		long start = (System.currentTimeMillis() - Constants.TICK_LIFE) * collisionSize;
		clearWindow(start);
		Stats stats = calculateForSnapshot(snapshotWindow(start));
		statsRef.set(stats);
	}

	@Override
	public void run() {
		calculate();
	}

	private void clearWindow(long start) {
		tsPriceMap.headMap(start).clear();
	}

	ConcurrentNavigableMap<Long, Double> snapshotWindow(long start) {
		ConcurrentNavigableMap<Long, Double> map = tsPriceMap.tailMap(start);
		return (map != null ? map : new ConcurrentSkipListMap<Long, Double>());
	}

	public Stats calculateForSnapshot(ConcurrentNavigableMap<Long, Double> allTicksInCurrentWindow) {
		DoubleSummaryStatistics statistics = allTicksInCurrentWindow.values().stream().mapToDouble(n -> n)
				.summaryStatistics();
		return new Stats(statistics.getAverage(), statistics.getMax(), statistics.getMin(), statistics.getCount());
	}

}
