package com.binnosoc.simulation.sim;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("sim")
@RequiredArgsConstructor
@Tag(name = "Simulation")
public class SimController {
	
	private final SimService service;
	
    @PostMapping("/calc-payment")
    public ResponseEntity<SimResponse> calcPayment(
            @RequestBody SimRequest request
    ) {
        return ResponseEntity.ok(service.calcMonthly(request));
    }
}
