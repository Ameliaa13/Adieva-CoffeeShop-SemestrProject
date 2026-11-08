package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserModel;
import utils.CsrfUtils;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebFilter("/*")
public class SecurityFilter implements Filter {

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/", "/sign-in", "/sign-up", "/error",
            "/menu", "/menu/item",
            "/events", "/event",
            "/promotions", "/promotion"
    );

    private static final List<String> PUBLIC_PREFIXES =
            List.of("/css/", "/img/", "/js/");

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contextPath = httpRequest.getContextPath();
        String path = getPath(httpRequest);

        if (path.startsWith("/jsp/") || path.startsWith("/WEB-INF/")) {
            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        UserModel user = session != null
                && session.getAttribute("user") instanceof UserModel currentUser
                ? currentUser
                : null;

        boolean isAdminPath = path.equals("/admin") || path.startsWith("/admin/");

        if (isAdminPath) {
            if (user == null) {
                httpResponse.sendRedirect(contextPath + "/sign-in");
                return;
            }

            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                httpResponse.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "У вас нет доступа к панели администратора."
                );
                return;
            }
        } else if (user == null && !isPublic(path)) {
            httpResponse.sendRedirect(contextPath + "/sign-in");
            return;
        }

        boolean isAdmin = user != null
                && "ADMIN".equalsIgnoreCase(user.getRole());
        boolean isCartOrFavorites =
                path.equals("/cart") || path.equals("/favorites");

        if (isAdmin
                && isCartOrFavorites
                && "POST".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Администратору недоступно это действие."
            );
            return;
        }

        if (isSafeMethod(httpRequest.getMethod())) {
            if (!isStaticResource(path)) {
                CsrfUtils.getOrCreateToken(httpRequest);
            }
        } else if (!CsrfUtils.isValid(httpRequest)) {
            httpResponse.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Недействительный CSRF-токен. Обнови страницу и попробуй ещё раз."
            );
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isSafeMethod(String method) {
        return "GET".equalsIgnoreCase(method)
                || "HEAD".equalsIgnoreCase(method)
                || "OPTIONS".equalsIgnoreCase(method)
                || "TRACE".equalsIgnoreCase(method);
    }

    private boolean isStaticResource(String path) {
        return PUBLIC_PREFIXES.stream().anyMatch(path::startsWith);
    }

    private String getPath(HttpServletRequest request) {
        String servletPath = request.getServletPath() == null
                ? ""
                : request.getServletPath();
        String pathInfo = request.getPathInfo() == null
                ? ""
                : request.getPathInfo();
        String path = servletPath + pathInfo;

        return path.isEmpty() ? "/" : path;
    }

    private boolean isPublic(String path) {
        if (PUBLIC_PATHS.contains(path)) {
            return true;
        }

        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }
}