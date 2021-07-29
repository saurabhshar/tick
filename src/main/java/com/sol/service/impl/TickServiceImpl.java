package com.sol.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.sol.calc.CollisionStatsCalculator;
import com.sol.calc.StatsCalc;
import com.sol.service.TickService;
import com.sol.vo.Tick;
import com.sol.work.TickWorker;

@Service
public class TickServiceImpl implements TickService {

	private final ScheduledExecutorService allStatsCalcScheduler = Executors.newScheduledThreadPool(1);
	private final ExecutorService executor = Executors.newFixedThreadPool(10);
	private final StatsCalc calculator = new CollisionStatsCalculator();

	public TickServiceImpl() {
		allStatsCalcScheduler.scheduleAtFixedRate(calculator, 0, 1, TimeUnit.SECONDS);
	}

	public void add(Tick tick) {
		TickWorker worker = new TickWorker(tick);
		executor.submit(worker);
	}
}
