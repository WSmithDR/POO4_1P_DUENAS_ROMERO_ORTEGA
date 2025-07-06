package services.env;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvReader {
    Dotenv dotenv = Dotenv.load();
    // Creedenciales del correo del sistem
    public final String emailSystem = dotenv.get("EMAIL_SYSTEM");
    public final String passwordSystem = dotenv.get("PASSWORD_SYSTEM");

}