package com.sol.work;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicReference;

import com.sol.calc.StatsCalc;
import com.sol.calc.StatsCalculator;
import com.sol.vo.Stats;
import com.sol.vo.Tick;

public class SlidingWindow {

	private final ConcurrentSkipListMap<Long, Double> tickWindow = new ConcurrentSkipListMap<>();
	private final AtomicReference<Stats> statsRef = new AtomicReference<>();
	private final StatsCalc calculator = new StatsCalculator(tickWindow, statsRef, 1);

	public SlidingWindow addToQueue(Tick tick) {
		tickWindow.put(tick.getTimestamp(), tick.getPrice());
		return this;
	}

	public Stats getStats() {
		calculator.calculate();
		return statsRef.get();
	}
	
	public int getTickWindowsSize() {
		return tickWindow.size();
	}
	
}