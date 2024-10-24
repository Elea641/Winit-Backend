package com.cda.winit.auth.domain.service.interfaces;

import com.mailjet.client.errors.MailjetException;

public interface IEmailService {
    void sendMail(String receiverEmail, String body) throws MailjetException;
}
