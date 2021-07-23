package com.sol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sol.service.TickService;
import com.sol.vo.Stats;

@RestController
public class StatisticsController {

	@Autowired
	TickService tickService;

	@GetMapping(path = "/stats/ping")
	public HttpStatus ping() {
		return HttpStatus.OK;
	}

	@GetMapping(path = "/statistics/{instrument_identifier}", produces = "application/json")
	public ResponseEntity<Stats> getStatsForInstrument(@PathVariable String instrument_identifier) {
		// **** what if queried for wrong id or id doesnt exist.
		return ResponseEntity.ok(tickService.getStats(instrument_identifier));
	}

	@GetMapping(path = "/statistics", produces = "application/json")
	public ResponseEntity<Stats> getStats() {
		return ResponseEntity.ok(tickService.getStats());
	}

}
