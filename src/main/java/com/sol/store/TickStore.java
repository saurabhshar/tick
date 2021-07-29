package com.sol.store;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicReference;

import com.sol.vo.Stats;
import com.sol.work.SlidingWindow;

public class TickStore {

	/*
	 * Contains all ticks prices as per their timestamp.
	 */
	private final ConcurrentSkipListMap<Long, Double> allTickPrices = new ConcurrentSkipListMap<>();

	/*
	 * Contains all instruments and their sliding window.
	 */
	private final ConcurrentHashMap<String, SlidingWindow> instrumentsWindows = new ConcurrentHashMap<>();

	private final AtomicReference<Stats> allStatsRef = new AtomicReference<>();

	private final static TickStore tickStore = new TickStore();

	private TickStore() {
	}

	public static TickStore getInstance() {
		return tickStore;
	}

	public ConcurrentSkipListMap<Long, Double> getAllTickPrices() {
		return allTickPrices;
	}

	public ConcurrentHashMap<String, SlidingWindow> getInstrumentsWindows() {
		return instrumentsWindows;
	}

	public AtomicReference<Stats> getAllStatsRef() {
		return allStatsRef;
	}

}
