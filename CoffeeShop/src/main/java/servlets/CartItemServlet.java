package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserModel;
import service.CartItemService;
import service.impl.CartItemServiceImpl;

import java.io.IOException;


@WebServlet("/cart")
public class CartItemServlet extends HttpServlet {

    private final CartItemService cartItemService = CartItemServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        UserModel user = getSessionUser(req, resp);

        if(user == null){
            return;
        }

        req.setAttribute("cartItems", cartItemService.getAllItems(user.getId()));
        req.getRequestDispatcher("/jsp/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserModel user = getSessionUser(req, resp);

        if(user == null){
            return;
        }

        String action = req.getParameter("action");

        try{
            switch (action == null ? "" : action){
                case "add" -> {
                    Long itemId = getRequiredLong(req, "itemId");
                    cartItemService.addItem(user.getId(), itemId);
                }
                case "update" -> {
                    Long itemId = getRequiredLong(req, "itemId");
                    Integer quantity = Math.toIntExact(
                            getRequiredLong(req, "quantity")
                    );

                    if(quantity <= 0){
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "quantity must be more than null");
                        return;
                    }

                    cartItemService.updateQuantity(user.getId(), itemId, quantity);
                }
                case "delete" -> {
                    Long itemId = getRequiredLong(req, "itemId");
                    cartItemService.deleteItem(user.getId(), itemId);
                }
                case "clear" -> cartItemService.clearCart(user.getId());

                default -> {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
        }catch (NumberFormatException | ArithmeticException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
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

    private UserModel getSessionUser(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        HttpSession session = req.getSession(false);

        if(session == null || session.getAttribute("user") == null){
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return null;
        }
        return (UserModel) session.getAttribute("user");
    }


    private Long getRequiredLong(HttpServletRequest req, String parameterName){
        String value = req.getParameter(parameterName);

        if(value == null || value.isBlank()){
            throw new NumberFormatException();
        }

        return Long.parseLong(value);
    }
}
