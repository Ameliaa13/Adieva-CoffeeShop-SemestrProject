package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.Services;
import model.PromotionModel;
import service.PromotionService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@WebServlet(urlPatterns = {"/promotions", "/promotion"})
public class PromotionServlet extends HttpServlet {

    private PromotionService promotionService;

    @Override
    public void init() {
        Services services = Services.from(getServletContext());
        promotionService = services.get(PromotionService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if ("/promotion".equals(req.getServletPath())) {
            showPromotion(req, resp);
            return;
        }

        req.setAttribute("promotions", promotionService.getActivePromotions());
        req.getRequestDispatcher("/jsp/promotions.jsp").forward(req, resp);
    }

    private void showPromotion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idParameter = req.getParameter("id");

        if (idParameter == null || idParameter.isBlank()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Long id;

        try {
            id = Long.parseLong(idParameter);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (id <= 0) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Optional<PromotionModel> promotionOptional = promotionService.getPromotionById(id);

        if (promotionOptional.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        PromotionModel promotion = promotionOptional.get();
        LocalDate today = LocalDate.now();

        if (!promotion.isActive()
                || promotion.getStartDate() == null
                || promotion.getEndDate() == null
                || today.isBefore(promotion.getStartDate())
                || today.isAfter(promotion.getEndDate())) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("promotion", promotion);
        req.getRequestDispatcher("/jsp/promotion-item.jsp").forward(req, resp);
    }
}