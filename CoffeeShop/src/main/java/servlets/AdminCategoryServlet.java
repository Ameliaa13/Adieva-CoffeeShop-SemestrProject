package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.Services;
import model.CategoryModel;
import service.MenuService;
import utils.AdminServletUtils;
import utils.ValidationUtils;

import java.io.IOException;

@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {

    private MenuService menuService;

    @Override
    public void init() {
        Services services = Services.from(getServletContext());
        menuService = services.get(MenuService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        showCategories(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("save".equals(action)) {
                saveCategory(req);
            } else if ("delete".equals(action)) {
                deleteCategory(req);
            } else {
                throw new IllegalArgumentException("Неизвестное действие.");
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories");

        } catch (ValidationUtils.ValidationException | IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            showCategories(req, resp);

        } catch (RuntimeException e) {
            getServletContext().log("Не удалось выполнить действие с категорией.", e);
            req.setAttribute("errorMessage","Не удалось выполнить действие. Попробуй ещё раз.");
            showCategories(req, resp);
        }
    }

    private void showCategories(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("categories", menuService.getAllCategories());
        String idParameter = req.getParameter("id");

        if (idParameter != null && !idParameter.isBlank()) {
            try {
                Long id = AdminServletUtils.requireLong(req, "id", "Номер категории");
                menuService.getCategoryById(id).ifPresentOrElse(
                        category -> req.setAttribute("editingCategory", category),
                        () -> req.setAttribute("errorMessage", "Категория не найдена."));

            } catch (IllegalArgumentException | ValidationUtils.ValidationException e) {
                req.setAttribute("errorMessage", e.getMessage());
            }
        }

        req.getRequestDispatcher("/jsp/admin/categories.jsp").forward(req, resp);
    }

    private void saveCategory(HttpServletRequest req) {
        String idParameter = req.getParameter("id");
        String name = AdminServletUtils.requireText(req, "name","Название категории");

        CategoryModel category = CategoryModel.builder()
                .name(name)
                .build();

        if (idParameter == null || idParameter.isBlank()) {
            menuService.createCategory(category);
            return;
        }

        Long id = AdminServletUtils.requireLong(req, "id", "Номер категории");

        boolean updated = menuService.updateCategoryById(id, category);

        if (!updated) {
            throw new IllegalArgumentException("Категория для изменения не найдена.");
        }
    }

    private void deleteCategory(HttpServletRequest req) {
        Long id = AdminServletUtils.requireLong(req, "id", "Номер категории");

        boolean deleted = menuService.deleteCategoryById(id);

        if (!deleted) {
            throw new IllegalArgumentException("Категория для удаления не найдена.");
        }
    }
}