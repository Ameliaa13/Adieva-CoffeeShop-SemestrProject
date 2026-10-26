package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MenuService;
import service.impl.MenuServiceImpl;

import java.io.IOException;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    private final MenuService menuService = MenuServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", menuService.getAllCategories());
        req.setAttribute("menuItems", menuService.getAllMenuItems());

        req.getRequestDispatcher("/jsp/menu.jsp").forward(req,resp);
    }

}
