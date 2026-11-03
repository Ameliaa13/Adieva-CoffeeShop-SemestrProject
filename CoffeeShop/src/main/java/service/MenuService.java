package service;

import lombok.Data;
import model.CategoryModel;
import model.MenuItemModel;

import java.util.List;
import java.util.Optional;


public interface MenuService {

    List<CategoryModel> getAllCategories();
    List<MenuItemModel> getAllMenuItems();
    List<MenuItemModel> getItemsByCategory(Long categoryId);
    Optional<MenuItemModel> getMenuItemById(Long id);
    void createMenuItem(MenuItemModel menuItem);
    boolean updateMenuItemById(Long id, MenuItemModel menuItem);
    boolean deleteMenuItemById(Long id);

}
