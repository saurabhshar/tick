package com.sol.service.impl;

import org.springframework.stereotype.Service;

import com.sol.service.StatisticsService;
import com.sol.store.TickStore;
import com.sol.vo.Stats;
import com.sol.work.SlidingWindow;
@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Override
	public Stats getStats() {
		return TickStore.getInstance().getAllStatsRef().get();
	}

	@Override
	public Stats getStats(String instrument) {
		SlidingWindow window = TickStore.getInstance().getInstrumentsWindows().getOrDefault(instrument,
				new SlidingWindow());
 		return window.getStats();
	}

}
