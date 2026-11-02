package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.PromotionService;
import service.impl.PromotionServiceImpl;

import java.io.IOException;

@WebServlet("/promotions")
public class PromotionServlet extends HttpServlet {

    private final PromotionService promotionService = PromotionServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("promotions", promotionService.getActivePromotions());

        req.getRequestDispatcher("/jsp/promotions.jsp").forward(req, resp);
    }
}
