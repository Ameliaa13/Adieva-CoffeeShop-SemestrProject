package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import listener.Services;
import model.UserModel;
import service.CartItemService;
import utils.ErrorUtils;
import utils.ValidationUtils;

import java.io.IOException;


@WebServlet("/cart")
public class CartItemServlet extends HttpServlet {

    private CartItemService cartItemService;

    @Override
    public void init() {
        Services services = Services.from(getServletContext());
        cartItemService = services.get(CartItemService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserModel user = getSessionUser(req, resp);

        if (user == null) {
            return;
        }

        req.setAttribute("cartItems", cartItemService.getAllItems(user.getId()));
        req.getRequestDispatcher("/jsp/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel user = getSessionUser(req, resp);

        if (user == null) {
            return;
        }

        Long userId = user.getId();
        String action = req.getParameter("action");

        try {
            switch (action == null ? "" : action) {
                case "add" -> {
                    Long itemId = getRequiredLong(req, "itemId");
                    cartItemService.addItem(userId, itemId);
                }
                case "update" -> {
                    Long itemId = getRequiredLong(req, "itemId");
                    Integer quantity = getRequiredInt(req, "quantity");

                    if (!cartItemService.updateQuantity(userId, itemId, quantity)) {
                        throw new ValidationUtils.ValidationException("itemId", "Товара нет в корзине.");
                    }
                }
                case "delete" -> {
                    Long itemId = getRequiredLong(req, "itemId");
                    cartItemService.deleteItem(userId, itemId);
                }
                case "clear" -> cartItemService.clearCart(userId);

                default -> {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
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
        } else if ("favorites".equals(returnTo)) {
            resp.sendRedirect(req.getContextPath() + "/favorites");
        } else {
            resp.sendRedirect(req.getContextPath() + "/cart");
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

    private Integer getRequiredInt(HttpServletRequest req, String parameterName) {
        String value = req.getParameter(parameterName);

        if (value == null || value.isBlank()) {
            throw new NumberFormatException();
        }

        return Integer.parseInt(value.trim());
    }
}