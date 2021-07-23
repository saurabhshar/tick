package com.sol.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sol.service.TickService;
import com.sol.service.TickValidatorService;
import com.sol.vo.Tick;

@RestController
public class TickController {

	@Autowired
	TickService tickService;

	@Autowired
	TickValidatorService validatorService;

	@GetMapping(path = "/ping")
	public HttpStatus ping() {
		return HttpStatus.OK;
	}

	@PostMapping(path = "/tick", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> consume(@RequestBody Tick tick) {
		long start = System.currentTimeMillis();
		boolean validTime = validatorService.validateTimeStamp(tick, start);
		if (!validTime) // ****fix this ..instead of boolean, validation itself can create and send
						// response.
			return ResponseEntity.status(204).build();
		validatorService.validateStructure(tick);
		tickService.add(tick);
		return ResponseEntity.status(201).build();
		// **** also, add check to see all fields are ok else send back bad msg error
	}

}
