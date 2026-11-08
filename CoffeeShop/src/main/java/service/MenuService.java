package service;

import model.CategoryModel;
import model.MenuItemModel;

import java.util.List;
import java.util.Optional;


public interface MenuService {

    void createCategory(CategoryModel category);

    List<CategoryModel> getAllCategories();

    Optional<CategoryModel> getCategoryById(Long id);

    boolean updateCategoryById(Long id, CategoryModel category);

    boolean deleteCategoryById(Long id);


    void createMenuItem(MenuItemModel menuItem);

    List<MenuItemModel> getAllMenuItems();

    List<MenuItemModel> getItemsByCategory(Long categoryId);

    Optional<MenuItemModel> getMenuItemById(Long id);

    boolean updateMenuItemById(Long id, MenuItemModel menuItem);

    boolean deleteMenuItemById(Long id);



}
