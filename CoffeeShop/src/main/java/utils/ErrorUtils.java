package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ErrorUtils {

    public static void redirectToError(HttpServletRequest req, HttpServletResponse resp, String message) throws IOException {

        String encodeMsg = URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(req.getContextPath() + "/error?msg=" + encodeMsg);
    }
}
