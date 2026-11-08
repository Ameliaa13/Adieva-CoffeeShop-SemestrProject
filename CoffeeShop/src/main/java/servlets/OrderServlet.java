package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import listener.Services;
import model.OrderModel;
import model.UserModel;
import service.OrderService;
import utils.ErrorUtils;
import utils.ValidationUtils;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/checkout", "/order-success"})
public class OrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        Services services = Services.from(getServletContext());
        orderService = services.get(OrderService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserModel user = getSessionUser(req, resp);
        if (user == null) {
            return;
        }

        if ("/order-success".equals(req.getServletPath())) {
            showSuccess(req, resp, user);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserModel user = getSessionUser(req, resp);
        if (user == null) {
            return;
        }

        if (!"/checkout".equals(req.getServletPath())) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        try {
            Long orderId = orderService.checkout(user.getId());
            resp.sendRedirect(req.getContextPath() + "/order-success?id=" + orderId);
        } catch (ValidationUtils.ValidationException e) {
            resp.sendRedirect(req.getContextPath() + "/cart?checkout=empty");
        } catch (RuntimeException e) {
            getServletContext().log("Не удалось оформить заказ.", e);
            resp.sendRedirect(req.getContextPath() + "/cart?checkout=error");
        }
    }

    private void showSuccess(HttpServletRequest req, HttpServletResponse resp, UserModel user) throws ServletException, IOException {

        Long orderId = parseId(req.getParameter("id"));

        if (orderId == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<OrderModel> order = orderService.getUserOrder(user.getId(), orderId);

        if (order.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("order", order.get());
        req.getRequestDispatcher("/jsp/order-complete.jsp").forward(req, resp);
    }

    private UserModel getSessionUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);

        if (session == null || !(session.getAttribute("user") instanceof UserModel user)) {
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return null;
        }

        return user;
    }

    private Long parseId(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            long id = Long.parseLong(value.trim());
            return id > 0 ? id : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}