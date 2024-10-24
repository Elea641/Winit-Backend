package com.cda.winit.auth.domain.service.interfaces;

import com.cda.winit.user.domain.entity.User;
import com.mailjet.client.errors.MailjetException;

public interface IPasswordForgottenService {

    String tokenGenerator(String email) throws MailjetException;

    String tokenProvider();

    void mailSender(User user, String token) throws MailjetException;

   void checkTokenValidityAndCreateNewPassword(String token, User user);
}
