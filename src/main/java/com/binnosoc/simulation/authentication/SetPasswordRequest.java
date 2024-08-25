package com.binnosoc.simulation.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetPasswordRequest {
	@Email(message = "Email is not well formatted")
	@NotEmpty(message = "Email is mandatory")
	@NotNull(message = "Email is mandatory")
	private String email;

}
