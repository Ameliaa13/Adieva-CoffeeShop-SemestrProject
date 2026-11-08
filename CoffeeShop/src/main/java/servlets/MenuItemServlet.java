package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.Services;
import model.MenuItemModel;
import service.MenuService;
import utils.ValidationUtils;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/menu/item")
public class MenuItemServlet extends HttpServlet {

    private MenuService menuService;

    @Override
    public void init() {
        Services services = Services.from(getServletContext());
        menuService = services.get(MenuService.class);
    }

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {

        String idParameter = req.getParameter("id");

        if (idParameter == null || idParameter.isBlank()) {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Не указан номер товара."
            );
            return;
        }

        Long id;

        try {
            id = Long.parseLong(idParameter.trim());
        } catch (NumberFormatException e) {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Некорректный номер товара."
            );
            return;
        }

        try {
            Optional<MenuItemModel> menuItem = menuService.getMenuItemById(id);

            if (menuItem.isEmpty()) {
                resp.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Товар не найден."
                );
                return;
            }

            req.setAttribute("menuItem", menuItem.get());
            req.getRequestDispatcher("/jsp/menu-item.jsp").forward(req, resp);

        } catch (ValidationUtils.ValidationException e) {
            resp.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }
}