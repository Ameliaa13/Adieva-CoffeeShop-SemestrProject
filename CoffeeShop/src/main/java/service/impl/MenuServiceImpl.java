package service.impl;

import model.CategoryModel;
import model.MenuItemModel;
import repository.CategoryRepository;
import repository.MenuItemRepository;
import repository.impl.CategoryRepositoryImpl;
import repository.impl.MenuItemRepositoryImpl;
import service.MenuService;

import java.util.List;
import java.util.Optional;

public class MenuServiceImpl implements MenuService {

    private final MenuItemRepository menuItemRepository = MenuItemRepositoryImpl.getInstance();
    private final CategoryRepository categoryRepository = CategoryRepositoryImpl.getInstance();

    private MenuServiceImpl(){}
    private static class Holder{
        private static final MenuServiceImpl INSTANCE = new MenuServiceImpl();
    }
    public static MenuServiceImpl getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    public List<CategoryModel> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<MenuItemModel> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public List<MenuItemModel> getItemsByCategory(Long categoryId) {
        return menuItemRepository.findByCategoryId(categoryId);
    }

    @Override
    public Optional<MenuItemModel> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }
}
