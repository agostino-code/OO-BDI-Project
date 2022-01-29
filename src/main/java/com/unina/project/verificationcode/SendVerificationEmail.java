package com.unina.project.verificationcode;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class SendVerificationEmail {
    public String SendEmail(String destinatario) {
        String mittente = "projectjava89@gmail.com";
        String password = "ProjectJavaAlessandroAgostino";
        String host = "smtp.gmail.com";
        String oggetto = "Formazione Facile - Codice di Verifica";
        String testo = """
                Hai richiesto un codice di Verifica?

                Se non hai richiesto nessun codice di Verifica ignora questa mail.
                Se hai richiesto il Codice di Verifica

                Il tuo codice di verifica è :""";
        Random r = new Random();
        int codiceverifica = r.nextInt(999999);
        testo = testo.concat(Integer.toString(codiceverifica));
        Properties p = System.getProperties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", host);
        p.put("mail.smtp.port", 25);
        Session sessione = Session.getDefaultInstance(p, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mittente, password);
            }
        });

        try {
            MimeMessage mail = new MimeMessage(sessione);
            mail.setFrom(new InternetAddress(mittente));
            mail.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(destinatario));
            mail.setSubject(oggetto);
            mail.setText(testo);
            Transport.send(mail);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.toString(codiceverifica);
    }

}
