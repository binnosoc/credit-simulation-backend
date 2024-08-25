package com.binnosoc.simulation.authentication;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.binnosoc.simulation.email.EmailService;
import com.binnosoc.simulation.email.EmailTemplateName;
import com.binnosoc.simulation.model.Token;
import com.binnosoc.simulation.model.User;
import com.binnosoc.simulation.repository.TokenRepository;
import com.binnosoc.simulation.repository.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${application.mailing.frontend.reset-password-url}")
	private String resetPasswordUrl;

//	public PasswordResetService(UserRepository userRepository, TokenRepository tokenRepository,
//			EmailService emailService) {
//		this.userRepository = userRepository;
//		this.tokenRepository = tokenRepository;
//		this.emailService = emailService;
//	}

	@Transactional
	public AuthenticationResponse createPasswordResetTokenForUser(SetPasswordRequest request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		String token = UUID.randomUUID().toString();

		var resetToken = Token.builder().token(token).createdAt(LocalDateTime.now())
				.expiresAt(LocalDateTime.now().plusMinutes(15)).user(user).build();

		tokenRepository.save(resetToken);

		try {
			sendResetEmail(user, token);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return AuthenticationResponse.builder().token(token).build();
	}

	private void sendResetEmail(User user, String token) throws MessagingException {

		emailService.sendEmail(user.getEmail(), user.getFullName(), EmailTemplateName.RESET_PASSWORD,
				resetPasswordUrl + "?t=" + token, token, "Reset password");
	}

	public String validatePasswordResetToken(String token) {

		Token passToken = tokenRepository.findByToken(token)
				// todo exception has to be defined
				.orElseThrow(() -> new RuntimeException("Invalid token"));
		if (LocalDateTime.now().isAfter(passToken.getExpiresAt())) {

			throw new RuntimeException("Reset password token has expired.");
		}

		return "valid";
	}

	public User getUserByPasswordResetToken(String token) {

		Token savedToken = tokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Invalid token"));
		if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {

			throw new RuntimeException("Reset password token has expired.");
		}

		var user = userRepository.findById(savedToken.getUser().getId())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return user;

	}

	public void changeUserPassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	public void deletePasswordResetToken(String token) {
		Token savedToken = tokenRepository.findByToken(token)
				.orElseThrow(() -> new RuntimeException("Invalid token"));
		tokenRepository.deleteById(savedToken.getId());
	}

}
