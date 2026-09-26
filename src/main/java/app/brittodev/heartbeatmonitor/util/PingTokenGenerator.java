package app.brittodev.heartbeatmonitor.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PingTokenGenerator {

    private final SecureRandom gen = new SecureRandom();

    public String generate() {
        byte[] bytes = new byte[32];
        gen.nextBytes(bytes);
        String formated = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);

        return formated;
    }
}
