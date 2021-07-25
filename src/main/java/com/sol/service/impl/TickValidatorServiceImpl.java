package com.sol.service.impl;

import org.springframework.stereotype.Service;

import com.sol.service.TickValidatorService;
import com.sol.utils.Constants;
import com.sol.vo.Tick;

@Service
public class TickValidatorServiceImpl implements TickValidatorService {

	@Override
	public boolean validateTimeStamp(Tick tick, long now) {
		return tick.getTimestamp() > (now - Constants.TICK_LIFE);
	}

	@Override
	public boolean validateRequestSchema(Tick tick) {
		return !(tick.getInstrument() == null || "".equals(tick.getInstrument()) || tick.getPrice() <= 0
				|| Double.isNaN(tick.getPrice()) || (tick.getTimestamp() <= 0));
	}

}
