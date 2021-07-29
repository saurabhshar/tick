package com.sol.service;

import org.springframework.stereotype.Service;

import com.sol.vo.Tick;

@Service
public interface TickService {

	public void add(Tick tick);
}
