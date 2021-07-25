package com.sol.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
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
import com.sol.work.StatsWorker;

@Service
public class TickServiceImpl implements TickService {

	private final ConcurrentSkipListMap<Long, Double> allTickPrices = new ConcurrentSkipListMap<>();
	private final AtomicReference<Stats> allStatsRef = new AtomicReference<>();

	private final ConcurrentHashMap<String, SlidingWindow> instrumentsWindows = new ConcurrentHashMap<>();

	private final ScheduledExecutorService allStatsCalcScheduler = Executors.newScheduledThreadPool(1);
	private final ExecutorService executor = Executors.newFixedThreadPool(10);

	public TickServiceImpl() {
		StatsCalc calculator = new StatsCalculator(allTickPrices, allStatsRef, Constants.COLLISION_SIZE);
		allStatsCalcScheduler.scheduleAtFixedRate(calculator, 0, 1, TimeUnit.SECONDS);
	}

	public void add(Tick tick) {
		StatsWorker worker = new StatsWorker(tick, instrumentsWindows, allTickPrices);
		executor.submit(worker);
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
