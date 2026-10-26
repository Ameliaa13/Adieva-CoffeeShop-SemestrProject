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
                requestURI.startsWith(contextPath + "/css/") ||
                requestURI.equals(contextPath + "/error") ||
                requestURI.startsWith(contextPath + "/js/");

        if(user != null || isPublicPage){
            chain.doFilter(request, response);
        }else {
            httpResponse.sendRedirect(contextPath + "/sign-in");
        }
    }
}
