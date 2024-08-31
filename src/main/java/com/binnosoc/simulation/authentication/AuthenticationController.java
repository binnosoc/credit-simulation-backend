package com.binnosoc.simulation.authentication;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.binnosoc.simulation.model.User;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

	private final AuthenticationService service;
	private final PasswordResetService passwordResetService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
		service.register(request);
		return ResponseEntity.accepted().build();
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@GetMapping("/activate-account")
	public void confirm(@RequestParam String token) throws MessagingException {
		service.activateAccount(token);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<AuthenticationResponse> resetPassword(@RequestBody SetPasswordRequest request)
			throws MessagingException {
		return ResponseEntity.ok(passwordResetService.createPasswordResetTokenForUser(request));
	}

	@GetMapping("/reset-password")
	public ResponseEntity<?> showResetPasswordPage(@RequestParam String token) {
		String result = passwordResetService.validatePasswordResetToken(token);

		if (!"valid".equals(result)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(result.equals("invalidToken") ? "Invalid token" : "Expired token"));
		}

		return ResponseEntity.ok(new SuccessResponse("reset-password", token));

	}

	@GetMapping("/search")
	public Optional<User> getUserByEmail(@RequestParam String email) {

		return service.getUserByEmail(email);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> handlePasswordReset(@RequestParam String token, @RequestParam String newPassword, Model model) {
		String result = passwordResetService.validatePasswordResetToken(token);

		if (!"valid".equals(result)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(result.equals("invalidToken") ? "Invalid token" : "Expired token"));
		}

//		return ResponseEntity.ok(new SuccessResponse("reset-password", token));

		Optional<User> user = Optional.ofNullable(passwordResetService.getUserByPasswordResetToken(token));

		if (user.isPresent()) {
			passwordResetService.changeUserPassword(user.get(), newPassword);
			passwordResetService.deletePasswordResetToken(token);

			
			return ResponseEntity.ok(new SuccessResponse("ok", token));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse(result.equals("error") ? "Error" : "Error Try Again"));
		}
	}

	

	

}
