package com.sol.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.sol.calc.StatsCalc;
import com.sol.calc.StatsCalculator;
import com.sol.service.TickService;
import com.sol.utils.Constants;
import com.sol.vo.Stats;
import com.sol.vo.Tick;
import com.sol.work.SlidingWindow;

@Service
public class TickServiceImpl implements TickService {

	private final ConcurrentSkipListMap<Long, Double> allTickPrices = new ConcurrentSkipListMap<>();
	private final ScheduledExecutorService allStatsCalcScheduler = Executors.newScheduledThreadPool(1);
	private final AtomicReference<Stats> allStatsRef = new AtomicReference<>();

	private final ConcurrentHashMap<String, SlidingWindow> instrumentsWindows = new ConcurrentHashMap<>();

	public TickServiceImpl() {
		StatsCalc calculator = new StatsCalculator(allTickPrices, allStatsRef, Constants.COLLISION_SIZE);
		allStatsCalcScheduler.scheduleAtFixedRate(calculator, 0, 1, TimeUnit.SECONDS);
	}

	public void add(Tick tick) {
		addWithUniqueTickTime(tick.getTimestamp(), tick.getPrice());
		
		String instrument = tick.getInstrument();
		SlidingWindow window = null;
		if (!instrumentsWindows.contains(instrument)) {
			window = new SlidingWindow();
			window.addToQueue(tick);
		}
		instrumentsWindows.merge(instrument, window, (w, t) -> w.addToQueue(tick));
		
	}

	/*
	 * Takes care of collision, and always generates a unique key for map - This is
	 * only for all ticks, for individual instrument this is not
	 * possible(assumption - per second tick generation, but could be implemented if
	 * generation is fast)
	 */
	private long addWithUniqueTickTime(long timestamp, double price) {
		long index = timestamp * Constants.COLLISION_SIZE;
		long maxblock = index + Constants.COLLISION_SIZE - 1;
		while (true) {
			Double val = allTickPrices.putIfAbsent(index, price);
			if (val == null || index > maxblock)
				break;
			else {
				index = index + 1;
			}
		}

		return index;
	}

	public Stats getStats(String instrument) {
		SlidingWindow window = instrumentsWindows.getOrDefault(instrument, new SlidingWindow());
		return window.getStats();
	}

	@Override
	public Stats getStats() {
		return allStatsRef.get();
	}
}
