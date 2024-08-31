package com.binnosoc.simulation.sim;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimService {

	public SimResponse calcMonthly(SimRequest request) {

		var amount = request.getAmount();
		var interestRate = request.getInterestRate() / 100.0;
		var duration = request.getDuration();

		var monthlyInterestRate = interestRate / 12.0;
		var payment = (amount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -duration));

		return SimResponse.builder().monthly(payment).build();

	}

}
