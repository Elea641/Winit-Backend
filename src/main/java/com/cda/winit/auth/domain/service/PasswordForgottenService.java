package com.cda.winit.auth.domain.service;

import com.cda.winit.auth.domain.entity.PasswordToken;
import com.cda.winit.auth.domain.service.interfaces.IEmailService;
import com.cda.winit.auth.domain.service.interfaces.IPasswordForgottenService;
import com.cda.winit.auth.infrastructure.exception.PasswordNotSimilarException;
import com.cda.winit.auth.infrastructure.exception.PasswordTokenException;
import com.cda.winit.auth.infrastructure.repository.PasswordTokenRepository;
import com.cda.winit.user.domain.entity.User;
import com.cda.winit.user.infrastructure.repository.UserRepository;
import com.mailjet.client.errors.MailjetException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
public class PasswordForgottenService implements IPasswordForgottenService {

    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final IEmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordForgottenService(UserRepository userRepository, PasswordTokenRepository passwordTokenRepository, IEmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieve a user by email
     * Create a new PasswordToken instance linked to that user
     * @param {String} email
     * @return {String} token
     */
    @Override
    public String tokenGenerator(String email) throws MailjetException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Utilisateur introuvable");
        }

        User user = optionalUser.get();

        String token = tokenProvider();
        LocalDateTime localDateTime = LocalDateTime.now();
        PasswordToken passwordToken = new PasswordToken();

        passwordToken.setToken(token);
        passwordToken.setUser(user);
        passwordToken.setDateOfExpiration(localDateTime.plusHours(24));

        passwordTokenRepository.save(passwordToken);
        userRepository.save(user);

        mailSender(user, token);
        return token;
    }

    /**
     * Generate a string and encode it to base64
     * without padding, to avoid URL problems
     * @return {String} token
     */
    @Override
    public String tokenProvider() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[100 * 3/4];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }


    /**
     * Send mail to the user, with the token in mail body
     * @param {User} user
     * @param {String} token
     * @throws MailjetException
     */
    @Override
    public void mailSender(User user, String token) throws MailjetException {
        String message = "Pour réinitialiser votre mot de passe, veuillez cliquer sur ce lien : ";
        String redirectUrl = "http://localhost:4200/auth/new-password/" + token;
        String mailBody = message + redirectUrl;
        emailService.sendMail(user.getEmail(), mailBody);
    }

    @Override
    public void checkTokenValidityAndCreateNewPassword(String token, User user) {
        PasswordToken passwordToken = passwordTokenRepository.findByToken(token);

        if (passwordToken == null || LocalDateTime.now().isAfter(passwordToken.getDateOfExpiration()) || passwordToken.isChanged()) {
            throw new PasswordTokenException();
        }

        User userToPersist = passwordToken.getUser();

        if (!Objects.equals(user.getPlainPassword(), user.getPlainPasswordVerification())) {
            throw new PasswordNotSimilarException();
        }

        String hashedPassword = passwordEncoder.encode(user.getPlainPassword());
        userToPersist.setPassword(hashedPassword);
        passwordToken.setChanged(true);
        passwordTokenRepository.save(passwordToken);
        userRepository.save(userToPersist);
    }

}
