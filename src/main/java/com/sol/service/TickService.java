package com.sol.service;

import org.springframework.stereotype.Service;

import com.sol.vo.Stats;
import com.sol.vo.Tick;

@Service
public interface TickService {

	public void add(Tick tick);
	public Stats getStats();
	public Stats getStats(String instrumentId);
}
