package com.binnosoc.simulation.sim;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SimRequest {
	
    @NotEmpty(message = "amount is mandatory")
    @NotNull(message = "amount is mandatory")
    private Double amount;
    
    @NotEmpty(message = "interestRate is mandatory")
    @NotNull(message = "interestRate is mandatory")
    private Integer interestRate;
    
    @NotEmpty(message = "duration is mandatory")
    @NotNull(message = "duration is mandatory")
    private Integer duration;
}
