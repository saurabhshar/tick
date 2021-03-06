package com.sol.service;

import org.springframework.stereotype.Service;

import com.sol.vo.Tick;

@Service
public interface TickValidatorService {

	public boolean validateTimeStamp(Tick tick, long now);

	public boolean validateRequestSchema(Tick tick);
}
