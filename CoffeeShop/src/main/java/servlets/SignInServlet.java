package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserModel;
import service.AuthService;
import service.impl.AuthServiceImpl;
import utils.ErrorUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/sign-in")
public class SignInServlet extends HttpServlet {

    private final AuthService authService = AuthServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/sign-in.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if(email == null || email.isBlank() ||
        password == null || password.isBlank()){
            ErrorUtils.redirectToError(req, resp, "Все поля должны быть заполнены!");
            return;
        }

        Optional<UserModel> auth = authService.authenticate(email,password);

        if (auth.isEmpty()){
            ErrorUtils.redirectToError(req, resp, "Неверный логин или пароль!");
            return;
        }

        UserModel user = auth.get();

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);


        //TODO: добавить путь к сайту
        resp.sendRedirect(req.getContextPath() + "/menu");
    }
}
