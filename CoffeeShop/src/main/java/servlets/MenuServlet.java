package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import listener.Services;
import model.CartItemModel;
import model.FavoriteModel;
import model.UserModel;
import service.CartItemService;
import service.FavoriteService;
import service.MenuService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    private MenuService menuService;
    private CartItemService cartItemService;
    private FavoriteService favoriteService;

    @Override
    public void init() {
        Services services = Services.from(getServletContext());
        menuService = services.get(MenuService.class);
        cartItemService = services.get(CartItemService.class);
        favoriteService = services.get(FavoriteService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", menuService.getAllCategories());
        req.setAttribute("menuItems", menuService.getAllMenuItems());

        Map<Long, Integer> cartQuantities = new HashMap<>();
        Map<Long, Boolean> favoriteItemIds = new HashMap<>();
        HttpSession session = req.getSession(false);
        UserModel user = session == null
                ? null
                : (UserModel) session.getAttribute("user");

        if (user != null) {
            for (CartItemModel cartItem : cartItemService.getAllItems(user.getId())) {
                cartQuantities.put(
                        cartItem.getMenuItemId(),
                        cartItem.getQuantity()
                );
            }

            for (FavoriteModel favorite : favoriteService.getAllFavorites(user.getId())) {
                favoriteItemIds.put(favorite.getMenuItemId(), true);
            }
        }

        req.setAttribute("cartQuantities", cartQuantities);
        req.setAttribute("favoriteItemIds", favoriteItemIds);

        req.getRequestDispatcher("/jsp/menu.jsp").forward(req,resp);
    }

}