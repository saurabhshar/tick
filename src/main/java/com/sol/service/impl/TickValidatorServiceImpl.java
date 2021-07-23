package com.sol.service.impl;

import org.springframework.stereotype.Service;

import com.sol.service.TickValidatorService;
import com.sol.utils.Constants;
import com.sol.vo.Tick;

@Service
public class TickValidatorServiceImpl implements TickValidatorService {

	@Override
	public boolean validateTimeStamp(Tick tick, long start) {
		if (start > (tick.getTimestamp() + Constants.TICK_LIFE)) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean validateStructure(Tick tick) {
		// TODO Auto-generated method stub // ****
		// This one returns bad message structure if validation fails.
		return false;
	}

}
