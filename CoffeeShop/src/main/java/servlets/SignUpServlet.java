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
import java.time.LocalDate;

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

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String birthDateStr = req.getParameter("birth_date");
        String role = req.getParameter("role");


        if(email == null || email.isBlank()||
        password == null || password.isBlank() ||
        firstName == null || firstName.isBlank() ||
        lastName == null || lastName.isBlank() ||
                birthDateStr == null || birthDateStr.isBlank() ||
        role == null || role.isBlank()){
            ErrorUtils.redirectToError(req, resp,"Все поля формы регистрации обязательны к заполнению!" );
            return;
        }

        if(userRepository.existByEmail(email)){
            ErrorUtils.redirectToError(req, resp, "Пользователь с почтой '" + email + "' уже существует!");
            return;
        }
        LocalDate birthDate = LocalDate.parse(birthDateStr);

        authService.register(email, password, firstName, lastName, birthDate, role);
        //TODO: добавить путь
        resp.sendRedirect(req.getContextPath());

    }
}
