package com.sol.service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.sol.service.impl.TickValidatorServiceImpl;
import com.sol.utils.Constants;
import com.sol.vo.Tick;

public class TickValidatorServiceTest {

	TickValidatorService validator = new TickValidatorServiceImpl();

	@Test
	public void testValidateTimestamp() {
		long now = System.currentTimeMillis();
		Tick tick = new Tick();
		tick.setTimestamp(now);
		assertTrue(validator.validateTimeStamp(tick, now));
		tick.setTimestamp(now - Constants.TICK_LIFE - 100);
		assertFalse(validator.validateTimeStamp(tick, now));
	}

	@Test
	public void testValidateStructure() {
		Tick tick = new Tick();
		tick.setInstrument("");
		assertFalse(validator.validateRequestSchema(tick));
		tick.setInstrument("IBM");
		tick.setPrice(0);
		assertFalse(validator.validateRequestSchema(tick));
		tick.setPrice(2.5);
		assertFalse(validator.validateRequestSchema(tick));
		tick.setTimestamp(System.currentTimeMillis());
		assertTrue(validator.validateRequestSchema(tick));
	}
}
