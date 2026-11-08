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

    private static final List<String> PUBLIC_PREFIXES = List.of("/css/", "/img/", "/js/");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contextPath = httpRequest.getContextPath();
        String path = getPath(httpRequest);

        if (path.startsWith("/jsp/") || path.startsWith("/WEB-INF/")) {
            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        UserModel user = (session != null && session.getAttribute("user") instanceof UserModel u) ? u : null;

        boolean isAdminPath = path.equals("/admin") || path.startsWith("/admin/");

        if (isAdminPath) {
            if (user == null) {
                httpResponse.sendRedirect(contextPath + "/sign-in");
                return;
            }

            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас нет доступа к панели администратора");
                return;
            }

            chain.doFilter(request, response);
            return;
        }

        if (user != null || isPublic(path)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(contextPath + "/sign-in");
        }
    }

    private String getPath(HttpServletRequest request) {
        String servletPath = request.getServletPath() == null ? "" : request.getServletPath();
        String pathInfo = request.getPathInfo() == null ? "" : request.getPathInfo();
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