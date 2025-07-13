package services.email;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import services.env.EnvReader;
import utils.Printers;

/**
 * Clase que gestiona el envío de correos electrónicos utilizando SMTP y credenciales del sistema.
 */
public class ManejadorEmail {
    EnvReader env = new EnvReader();
    private final String emailFrom = env.emailSystem;
    private final String appPassword = env.passwordSystem;

    private final Properties properties;
    private final Authenticator auth;

    /**
     * Constructor que configura las propiedades SMTP y la autenticación para el envío de correos.
     */
    public ManejadorEmail() {
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.user", emailFrom);
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.setProperty("mail.smtp.auth", "true");

        auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, appPassword);
            }
        };
    }

    /**
     * Envía un correo de texto plano.
     * @param mailTo destinatario
     * @param asunto asunto del correo
     * @param contenido cuerpo del correo
     * @return true si se envió correctamente, false si hubo error
     */
    public boolean enviarCorreo(String mailTo, String asunto, String contenido) {
        try {
            Session mSession = Session.getInstance(properties, auth);
            Message msg = new MimeMessage(mSession);

            msg.setFrom(new InternetAddress(emailFrom));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
            msg.setSubject(asunto);
            msg.setSentDate(new java.util.Date());
            msg.setContent(contenido, "text/html");

            Transport.send(msg);
            System.out.println("Correo enviado a " + mailTo);
            return true;
        } catch (Exception e) {
            Printers.printError("Error enviando correo: " + e.getMessage());
            return false;
        }
    }
}
