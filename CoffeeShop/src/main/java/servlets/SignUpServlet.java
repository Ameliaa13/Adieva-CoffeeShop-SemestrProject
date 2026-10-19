package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.UserRepository;
import repository.impl.UserRepositoryImpl;
import service.AuthService;
import service.impl.AuthServiceImpl;
import utils.ErrorUtils;

import java.io.IOException;

@WebServlet("/sign-up")
public class SignUpServlet extends HttpServlet {

    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final AuthService authService = AuthServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/sign-up.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if(username == null || username.isBlank()||
        password == null || password.isBlank()){
            ErrorUtils.redirectToError(req, resp,"Все поля формы регистрации обязательны к заполнению!" );
            return;
        }

        if(userRepository.existByUsername(username)){
            ErrorUtils.redirectToError(req, resp, "Пользователь с логином '" + username + "' уже существует!");
            return;
        }

        authService.register(username, password);
        //TODO: добавить путь
        resp.sendRedirect(req.getContextPath());

    }
}
