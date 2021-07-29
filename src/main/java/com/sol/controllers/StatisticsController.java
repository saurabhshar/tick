package com.sol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sol.service.StatisticsService;
import com.sol.vo.Stats;

@RestController
public class StatisticsController {

	@Autowired
	StatisticsService statsService;

	@GetMapping(path = "/stats/ping")
	public HttpStatus ping() {
		return HttpStatus.OK;
	}

	@GetMapping(path = "/statistics/{instrumentIdentifier}", produces = "application/json")
	public ResponseEntity<Stats> getStatsForInstrument(@PathVariable String instrumentIdentifier) {
		return ResponseEntity.ok(statsService.getStats(instrumentIdentifier));
	}

	@GetMapping(path = "/statistics", produces = "application/json")
	public ResponseEntity<Stats> getStats() {
		return ResponseEntity.ok(statsService.getStats());
	}

}
