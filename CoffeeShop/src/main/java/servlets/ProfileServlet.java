package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.EventModel;
import model.UserModel;
import service.OrderService;
import service.ProfileService;
import service.impl.OrderServiceImpl;
import service.impl.ProfileServiceImpl;
import utils.ValidationUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final ProfileService profileService = ProfileServiceImpl.getInstance();
    private final OrderService orderService = OrderServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel sessionUser = getSessionUser(req, resp);
        if (sessionUser == null) {
            return;
        }

        showProfile(req, resp, sessionUser);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        UserModel sessionUser = getSessionUser(req, resp);
        if (sessionUser == null) {
            return;
        }

        try {
            UserModel updatedUser = profileService.updateProfile(
                    sessionUser.getId(),
                    req.getParameter("email"),
                    req.getParameter("firstName"),
                    req.getParameter("lastName"),
                    req.getParameter("birthDate")
            );

            HttpSession session = req.getSession(false);
            session.setAttribute("user", UserModel.builder()
                    .id(updatedUser.getId())
                    .email(updatedUser.getEmail())
                    .firstName(updatedUser.getFirstName())
                    .lastName(updatedUser.getLastName())
                    .birthDate(updatedUser.getBirthDate())
                    .role(updatedUser.getRole())
                    .build());

            resp.sendRedirect(req.getContextPath() + "/profile?updated=true");
        } catch (ValidationUtils.ValidationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            showProfile(req, resp, sessionUser);
        }
    }

    private UserModel getSessionUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);

        if (session == null || !(session.getAttribute("user") instanceof UserModel user)) {
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return null;
        }

        return user;
    }

    private void showProfile(HttpServletRequest req, HttpServletResponse resp, UserModel sessionUser) throws ServletException, IOException {
        try {
            UserModel profile = profileService.getProfile(sessionUser.getId());
            List<EventModel> registeredEvents = profileService.getRegisteredEvents(sessionUser.getId());

            req.setAttribute("profile", profile);
            req.setAttribute("registeredEvents", registeredEvents);
            req.setAttribute("orderHistory", orderService.getUserOrders(sessionUser.getId()));
            req.getRequestDispatcher("/jsp/profile.jsp").forward(req, resp);
        } catch (ValidationUtils.ValidationException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}