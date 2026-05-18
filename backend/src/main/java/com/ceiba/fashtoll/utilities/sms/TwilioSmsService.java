package com.ceiba.fashtoll.utilities.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsService implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(TwilioSmsService.class);

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.from-number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
        logger.info("TwilioSmsService inicializado. Número de origen: {}", fromNumber);
    }

    @Override
    public void sendSms(String to, String message) {
        try {
            // Se prefija con "whatsapp:" para enrutar el mensaje a la API de WhatsApp de Twilio
            String whatsappTo = "whatsapp:" + to;
            String whatsappFrom = "whatsapp:" + fromNumber;

            Message smsMessage = Message.creator(
                    new PhoneNumber(whatsappTo),
                    new PhoneNumber(whatsappFrom),
                    message).create();

            logger.info("WhatsApp enviado exitosamente. SID: {} | Destino: {} | Estado: {}",
                    smsMessage.getSid(), whatsappTo, smsMessage.getStatus());

        } catch (Exception e) {
            logger.error("Error al enviar WhatsApp a {}: {}", to, e.getMessage());
        }
    }
}
