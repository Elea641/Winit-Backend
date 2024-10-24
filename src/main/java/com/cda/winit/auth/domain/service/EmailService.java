package com.cda.winit.auth.domain.service;

import com.cda.winit.auth.domain.service.interfaces.IEmailService;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TransactionalEmail;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Value("${mailjet.apikey}")
    private String apiKey;

    @Value("${mailjet.apiSecret}")
    private String apiSecret;


    @Override
    public void sendMail(String receiverEmail, String body) throws MailjetException {
        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiSecret)
                .build();

        MailjetClient client = new MailjetClient(options);

        TransactionalEmail email1 = TransactionalEmail
                .builder()
                .to(new SendContact(receiverEmail, "receiver"))
                .from(new SendContact("kevin.davoust@orange.fr", "WinIt"))
                .htmlPart("<h1>Mot de passe oublié</h1><p>" + body + "</p>")
                .subject("Mot de passe oublié")
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(email1)
                .build();

        SendEmailsResponse response = request.sendWith(client);
    }
}
