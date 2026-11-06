package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.MenuItemModel;
import service.MenuService;
import service.impl.MenuServiceImpl;
import utils.AdminServletUtils;
import utils.ValidationUtils;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/admin/menu")
public class AdminMenuServlet extends HttpServlet {

    private final MenuService menuService = MenuServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        showAdminMenu(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        try {
            if ("save".equals(action)) {
                saveMenuItem(req);
            } else if ("delete".equals(action)) {
                deleteMenuItem(req);
            } else {
                throw new IllegalArgumentException("Неизвестное действие.");
            }

        } catch (ValidationUtils.ValidationException | IllegalArgumentException e) {
            req.setAttribute("errorMessage", e.getMessage());
            showAdminMenu(req, resp);
            return;

        } catch (RuntimeException e) {
            getServletContext().log("Не удалось выполнить действие с меню.", e);
            req.setAttribute("errorMessage", "Не удалось выполнить действие. Проверь данные и попробуй ещё раз.");
            showAdminMenu(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/menu");
    }

    private void showAdminMenu(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("categories", menuService.getAllCategories());
        //req.setAttribute("menuItems", menuService.getAllMenuItems());

        String filterCategoryId = req.getParameter("filterCategoryId");
        Long selectedCategoryId = null;

        if(filterCategoryId != null && !filterCategoryId.isBlank()){
            try{
                Long categoryId = Long.parseLong(filterCategoryId.trim());

                if(categoryId >0 && menuService.getCategoryById(categoryId).isPresent()){
                    selectedCategoryId = categoryId;
                }else {
                    req.setAttribute("errorMessage",  "Выбранная категория не найдена.");
                }
            }catch (NumberFormatException e){
                req.setAttribute("errorMessage",  "Некорректный номер категории.");
            }
        }

        req.setAttribute("selectedCategoryId", selectedCategoryId);

        if(selectedCategoryId == null){
            req.setAttribute("menuItems", menuService.getAllMenuItems());
        }else{
            req.setAttribute("menuItems", menuService.getItemsByCategory(selectedCategoryId));
        }


        String idParameter = req.getParameter("id");

        if (idParameter != null && !idParameter.isBlank()) {
            try {
                Long id = Long.parseLong(idParameter.trim());
                Optional<MenuItemModel> menuItem = menuService.getMenuItemById(id);

                if (menuItem.isPresent()) {
                    req.setAttribute("editingItem", menuItem.get());
                } else {
                    req.setAttribute("errorMessage", "Товар не найден.");
                }

            } catch (NumberFormatException e) {
                req.setAttribute("errorMessage", "Некорректный номер товара.");

            } catch (ValidationUtils.ValidationException e) {
                req.setAttribute("errorMessage", e.getMessage());
            }
        }

        req.getRequestDispatcher("/jsp/admin/menu.jsp").forward(req, resp);
    }

    private void saveMenuItem(HttpServletRequest req) {
        String idParameter = req.getParameter("id");

        String name = AdminServletUtils.requireText(req, "name", "Название товара");

        Long categoryId = AdminServletUtils.requireLong(req, "categoryId", "Категория");

        Double price = AdminServletUtils.requireDouble(req, "price", "Цена");

        MenuItemModel menuItem = MenuItemModel.builder()
                .name(name)
                .categoryId(categoryId)
                .price(price)
                .description(emptyToNull(req.getParameter("description")))
                .imageUrl(emptyToNull(req.getParameter("imageUrl")))
                .calories(parseNutrition(req.getParameter("calories"), "Калории"))
                .proteins(parseNutrition(req.getParameter("proteins"), "Белки"))
                .fats(parseNutrition(req.getParameter("fats"), "Жиры"))
                .carbohydrates(parseNutrition(req.getParameter("carbohydrates"), "Углеводы"))
                .build();

        if (idParameter == null || idParameter.isBlank()) {
            menuService.createMenuItem(menuItem);
            return;
        }

        Long id = parseId(idParameter);
        boolean updated = menuService.updateMenuItemById(id, menuItem);

        if (!updated) {
            throw new IllegalArgumentException("Не удалось найти товар для изменения.");
        }
    }

    private void deleteMenuItem(HttpServletRequest req) {
        Long id = AdminServletUtils.requireLong(req, "id", "Номер товара");

        boolean deleted = menuService.deleteMenuItemById(id);

        if (!deleted) {
            throw new IllegalArgumentException("Не удалось найти товар для удаления.");
        }
    }

    private Double parseNutrition(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Заполни поле «" + fieldName + "».");
        }

        double number;

        try {
            number = Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("В поле «" + fieldName + "» должно быть число.");
        }

        if (!Double.isFinite(number)) {
            throw new IllegalArgumentException("В поле «" + fieldName + "» должно быть обычное число.");
        }

        return number;
    }

    private String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private Long parseId(String value) {
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный номер товара.");
        }
    }
}