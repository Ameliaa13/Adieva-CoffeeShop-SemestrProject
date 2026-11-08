package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.security.SecureRandom;
import java.util.Base64;

public final class CsrfUtils {

    private static final String SESSION_ATTRIBUTE = "csrfToken";
    private static final SecureRandom RANDOM = new SecureRandom();

    private CsrfUtils() {}

    public static String getOrCreateToken(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        String token = (String) session.getAttribute(SESSION_ATTRIBUTE);

        if (token == null) {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute(SESSION_ATTRIBUTE, token);
        }

        return token;
    }

    public static boolean isValid(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        if (session == null) {
            return false;
        }

        String expected = (String) session.getAttribute(SESSION_ATTRIBUTE);
        String actual = req.getParameter(SESSION_ATTRIBUTE);

        return expected != null && actual != null && expected.equals(actual);
    }
}