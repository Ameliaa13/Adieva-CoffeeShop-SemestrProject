package service;

import lombok.Data;
import model.CategoryModel;
import model.MenuItemModel;

import java.util.List;


public interface MenuService {

    List<CategoryModel> getAllCategories();
    List<MenuItemModel> getAllMenuItems();
    List<MenuItemModel> getItemsByCategory(Long categoryId);

}
