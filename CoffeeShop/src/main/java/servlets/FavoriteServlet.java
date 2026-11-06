package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserModel;
import service.FavoriteService;
import service.impl.FavoriteServiceImpl;
import utils.ErrorUtils;
import utils.ValidationUtils;

import java.io.IOException;

@WebServlet("/favorites")
public class FavoriteServlet extends HttpServlet {

    private final FavoriteService favoriteService = FavoriteServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserModel user = getSessionUser(req, resp);

        if (user == null) {
            return;
        }

        req.setAttribute("favorites", favoriteService.getAllFavorites(user.getId()));
        req.getRequestDispatcher("/jsp/favorites.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserModel user = getSessionUser(req, resp);

        if (user == null) {
            return;
        }

        String action = req.getParameter("action");

        try {
            Long itemId = getRequiredLong(req, "itemId");

            if ("add".equals(action)) {
                favoriteService.addItem(user.getId(), itemId);
            } else if ("delete".equals(action)) {
                favoriteService.deleteItem(user.getId(), itemId);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (ValidationUtils.ValidationException e) {
            ErrorUtils.redirectToError(req, resp, e.getMessage());
            return;
        }

        String returnTo = req.getParameter("returnTo");

        if ("menu".equals(returnTo)) {
            resp.sendRedirect(req.getContextPath() + "/menu");
        } else {
            resp.sendRedirect(req.getContextPath() + "/favorites");
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

    private Long getRequiredLong(HttpServletRequest req, String parameterName) {
        String value = req.getParameter(parameterName);

        if (value == null || value.isBlank()) {
            throw new NumberFormatException();
        }

        return Long.parseLong(value.trim());
    }
}