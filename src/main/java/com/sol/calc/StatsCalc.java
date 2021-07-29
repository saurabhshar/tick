package com.sol.calc;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicReference;

import com.sol.vo.Stats;

public interface StatsCalc extends Runnable{

	public void calculate(ConcurrentSkipListMap<Long, Double> tickWindow, AtomicReference<Stats> statsRef);
	
}
