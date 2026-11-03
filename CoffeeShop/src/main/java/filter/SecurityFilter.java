package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserModel;

import java.io.IOException;

@WebFilter("/*")
public class SecurityFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        HttpSession session = httpRequest.getSession(false);
        UserModel user = (session != null) ? (UserModel) session.getAttribute("user") : null;

        boolean isPublicPage = requestURI.equals(contextPath + "/") ||
                requestURI.equals(contextPath + "/sign-in") ||
                requestURI.equals(contextPath + "/sign-up") ||
                requestURI.equals(contextPath + "/error") ||
                requestURI.equals(contextPath + "/menu") ||
                requestURI.equals(contextPath + "/menu/item") ||
                requestURI.equals(contextPath + "/events") ||
                requestURI.equals(contextPath + "/promotions") ||

                requestURI.startsWith(contextPath + "/css/") ||
                requestURI.startsWith(contextPath + "/img/") ||
                requestURI.startsWith(contextPath + "/js/");

        if(user != null || isPublicPage){
            chain.doFilter(request, response);
        }else {
            httpResponse.sendRedirect(contextPath + "/sign-in");
        }

        boolean isAdminPath = requestURI.equals(contextPath + "/admin") ||
                requestURI.equals(contextPath + "/admin/");

        if(isAdminPath){
            if(user == null){
                httpResponse.sendRedirect(contextPath + "/sign-in");
                return;
            }

            if(user.getRole() == null || !user.getRole().equals("ADMIN")){
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "У вас нет доступа к панели администратора");
            }
            return;
        }

    }
}
