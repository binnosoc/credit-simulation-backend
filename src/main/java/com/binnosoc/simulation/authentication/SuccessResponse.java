package com.binnosoc.simulation.authentication;

import lombok.Data;

@Data
public class SuccessResponse {
	private String status;
	private String token;

	public SuccessResponse(String status, String token) {
		this.status = status;
		this.token = token;
	}
}
