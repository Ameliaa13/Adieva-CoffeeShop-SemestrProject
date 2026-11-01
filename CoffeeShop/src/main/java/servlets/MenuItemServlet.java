package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MenuItemModel;
import service.MenuService;
import service.impl.MenuServiceImpl;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/menu/item")
public class MenuItemServlet extends HttpServlet {

    private final MenuService menuService = MenuServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParameter = req.getParameter("id");

        if(idParameter == null || idParameter.isBlank()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long id;

        try {
            id = Long.parseLong(idParameter);
        }catch (NumberFormatException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Optional<MenuItemModel> model = menuService.getMenuItemById(id);

        if(model.isEmpty()){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("menuItem", model.get());
        req.getRequestDispatcher("/jsp/menu-item.jsp").forward(req, resp);

    }
}
