package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import listener.Services;
import model.UserModel;
import service.AuthService;
import utils.ErrorUtils;
import utils.ValidationUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/sign-in")
public class SignInServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() {
        Services services = Services.from(getServletContext());
        authService = services.get(AuthService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/sign-in.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Optional<UserModel> auth;

        try{
            auth = authService.authenticate(email, password);
        }catch (ValidationUtils.ValidationException e){
            ErrorUtils.redirectToError(req, resp, "Все поля должны быть заполнены!");
            return;
        }

        if (auth.isEmpty()){
            ErrorUtils.redirectToError(req, resp, "Неверный логин или пароль!");
            return;
        }

        UserModel user = auth.get();

        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("user", UserModel.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .role(user.getRole())
                .build());

        String destination = user.getRole() != null && user.getRole().equalsIgnoreCase("ADMIN")
                ? "/admin" : "/";

        resp.sendRedirect(req.getContextPath() + destination);
    }
}