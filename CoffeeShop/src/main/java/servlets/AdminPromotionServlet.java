package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.Services;
import model.PromotionModel;
import service.PromotionService;
import utils.AdminServletUtils;
import utils.ValidationUtils;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;

@WebServlet("/admin/promotions")
public class AdminPromotionServlet extends HttpServlet {

    private PromotionService promotionService;

    @Override
    public void init() {
        Services services = Services.from(getServletContext());
        promotionService = services.get(PromotionService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        showPromotions(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("save".equals(action)) {
                savePromotion(req);
            } else if ("delete".equals(action)) {
                deletePromotion(req);
            } else {
                throw new IllegalArgumentException("Неизвестное действие.");
            }

            resp.sendRedirect(req.getContextPath() + "/admin/promotions");

        } catch (ValidationUtils.ValidationException | IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            showPromotions(req, resp);

        } catch (RuntimeException e) {
            getServletContext().log("Не удалось выполнить действие с акцией.", e);
            req.setAttribute("errorMessage", "Не удалось выполнить действие. Проверь данные и попробуй ещё раз.");
            showPromotions(req, resp);
        }
    }

    private void showPromotions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("promotions", promotionService.getAllPromotions());

        String idParameter = req.getParameter("id");

        if (idParameter != null && !idParameter.isBlank()) {
            try {
                Long id = AdminServletUtils.requireLong(req, "id", "Номер акции");

                promotionService.getPromotionById(id).ifPresentOrElse(
                        promotion -> req.setAttribute("editingPromotion", promotion),
                        () -> req.setAttribute("errorMessage", "Акция не найдена.")
                );

            } catch (ValidationUtils.ValidationException | IllegalArgumentException e) {
                req.setAttribute("errorMessage", e.getMessage());
            }
        }

        req.getRequestDispatcher("/jsp/admin/promotions.jsp").forward(req, resp);
    }

    private void savePromotion(HttpServletRequest req) {
        String idParameter = req.getParameter("id");

        PromotionModel promotion = PromotionModel.builder()
                .title(AdminServletUtils.requireText(req, "title", "Название"))
                .shortDescription(AdminServletUtils.emptyToNull(req.getParameter("shortDescription")))
                .description(AdminServletUtils.requireText(req, "description", "Описание"))
                .imageUrl(AdminServletUtils.emptyToNull(req.getParameter("imageUrl")))
                .startDate(parseDate(req, "startDate", "Дата начала"))
                .endDate(parseDate(req, "endDate", "Дата окончания"))
                .active(req.getParameter("active") != null)
                .build();

        if (idParameter == null || idParameter.isBlank()) {
            promotionService.createPromotion(promotion);
            return;
        }

        Long id = AdminServletUtils.requireLong(req, "id", "Номер акции");

        if (!promotionService.updatePromotionById(id, promotion)) {
            throw new IllegalArgumentException("Акция для изменения не найдена.");
        }
    }

    private void deletePromotion(HttpServletRequest req) {
        Long id = AdminServletUtils.requireLong(req, "id", "Номер акции");

        if (!promotionService.deletePromotionById(id)) {
            throw new IllegalArgumentException("Акция для удаления не найдена.");
        }
    }

    private LocalDate parseDate(HttpServletRequest req, String parameterName, String fieldTitle) {
        String value = AdminServletUtils.requireText(req, parameterName, fieldTitle);

        try {
            return LocalDate.parse(value);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException(
                    "Укажи корректное значение поля «" + fieldTitle + "»."
            );
        }
    }
}