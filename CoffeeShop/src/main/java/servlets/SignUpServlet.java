package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.Services;
import service.AuthService;
import utils.ErrorUtils;
import utils.ValidationUtils;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;

@WebServlet("/sign-up")
public class SignUpServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() {
        Services services = Services.from(getServletContext());
        authService = services.get(AuthService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/sign-up.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String birthDateStr = req.getParameter("birth_date");

        LocalDate birthDate = null;
        if(birthDateStr != null && !birthDateStr.isBlank()){
            try{
                birthDate = LocalDate.parse(birthDateStr.trim());
            }catch (DateTimeException e){
                ErrorUtils.redirectToError(req, resp, "Некорректная дата рождения!");
                return;
            }
        }

        try{
            authService.register(email, password, firstName, lastName, birthDate);
        }catch (ValidationUtils.ValidationException e ){
            ErrorUtils.redirectToError(req, resp, e.getMessage());
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/sign-in");

    }
}