package com.david.api_tareas.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreo(String destino, String asunto, String mensaje) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(destino);
        mail.setSubject(asunto);
        mail.setText(mensaje);
        mail.setFrom("dferna40@gmail.com");
        mailSender.send(mail);
    }
}
