package com.sol.service.impl;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.sol.service.TickService;
import com.sol.vo.Stats;
import com.sol.vo.Tick;
import com.sol.work.SlidingWindow;

@Service
public class TickServiceImpl implements TickService {

	ConcurrentHashMap<String, SlidingWindow> instrumentsWindowMap = new ConcurrentHashMap<>();
	SlidingWindow commonTickWindow = new SlidingWindow();

	public void add(Tick tick) {
		String instrument = tick.getInstrument();
		SlidingWindow window = new SlidingWindow();
		instrumentsWindowMap.merge(instrument, window, (w, t) -> w.addToQueue(tick)); // **** is it thread-safe, concurrent
																				// and non-blocking
		commonTickWindow.addToQueue(tick);
	}

	public Stats getStats() {
		return commonTickWindow.getStats();
	}

	public Stats getStats(String instrument) {
		SlidingWindow window = instrumentsWindowMap.get(instrument);
		if (window != null) {
			return window.getStats();
		} else {
			throw new RuntimeException("Not found for instrument - " + instrument);
		}
	}
}
