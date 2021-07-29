package com.sol.service;

import com.sol.vo.Stats;

public interface StatisticsService {
	public Stats getStats();
	public Stats getStats(String instrumentId);
}
