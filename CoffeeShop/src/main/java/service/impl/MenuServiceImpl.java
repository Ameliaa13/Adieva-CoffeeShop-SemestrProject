package service.impl;

import model.CategoryModel;
import model.MenuItemModel;
import repository.CategoryRepository;
import repository.MenuItemRepository;
import repository.impl.CategoryRepositoryImpl;
import repository.impl.MenuItemRepositoryImpl;
import service.MenuService;
import utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class MenuServiceImpl extends ValidationUtils<MenuItemModel> implements MenuService {

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
    public void validate(MenuItemModel menuItemModel){
        requireNonNull(menuItemModel, "menuItem");

        requireNotBlank(menuItemModel.getName(), "name");
        requireMaxLength(menuItemModel.getName(), 100, "name");

        requireId(menuItemModel.getCategoryId(), "categoryId");
        requireNonNegative(menuItemModel.getPrice(), "price");

        requireMaxLength(menuItemModel.getImageUrl(), 256, "imageUrl");

        requireNonNegative(menuItemModel.getCalories(), "calories");
        requireNonNegative(menuItemModel.getProteins(), "proteins");
        requireNonNegative(menuItemModel.getFats(), "fats");
        requireNonNegative(menuItemModel.getCarbohydrates(), "carbohydrates");

    }

    public void validate(CategoryModel categoryModel) {
        requireNonNull(categoryModel, "categoryModel");

        requireNotBlank(categoryModel.getName(), "name");
        requireMaxLength(categoryModel.getName(), 50, "name");
    }


    private void requireCategoryExists(Long categoryId) {
        requireId(categoryId, "categoryId");

        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw error("categoryId", "Выбранная категория не найдена.");
        }
    }

    @Override
    public void createCategory(CategoryModel category) {
        validate(category);
        categoryRepository.save(category);
    }

    @Override
    public Optional<CategoryModel> getCategoryById(Long id) {
        requireId(id, "categoryId");
        return categoryRepository.findById(id);
    }

    @Override
    public List<CategoryModel> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public boolean updateCategoryById(Long id, CategoryModel category) {

        requireId(id, "categoryId");
        if(categoryRepository.findById(id).isEmpty()){
            return false;
        }
        validate(category);
        return categoryRepository.updateById(id, category);
    }

    @Override
    public boolean deleteCategoryById(Long id) {

        requireId(id, "categoryId");
        if(categoryRepository.findById(id).isEmpty()){
            return false;
        }

        if (!menuItemRepository.findByCategoryId(id).isEmpty()) {
            throw error(
                    "categoryId",
                    "Нельзя удалить категорию, пока в ней есть товары."
            );
        }
        return categoryRepository.deleteById(id);
    }



    @Override
    public void createMenuItem(MenuItemModel menuItem) {
        validate(menuItem);
        requireCategoryExists(menuItem.getCategoryId());

        menuItemRepository.save(menuItem);
    }

    @Override
    public List<MenuItemModel> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public List<MenuItemModel> getItemsByCategory(Long categoryId) {

        requireCategoryExists(categoryId);
        return menuItemRepository.findByCategoryId(categoryId);
    }

    @Override
    public Optional<MenuItemModel> getMenuItemById(Long id) {

        requireId(id, "menuItemId");
        return menuItemRepository.findById(id);
    }


    @Override
    public boolean updateMenuItemById(Long id, MenuItemModel menuItem) {

        requireId(id, "menuItemId");
        if(menuItemRepository.findById(id).isEmpty()){
            return false;
        }
        validate(menuItem);
        requireCategoryExists(menuItem.getCategoryId());
        return menuItemRepository.updateById(id, menuItem);
    }

    @Override
    public boolean deleteMenuItemById(Long id) {

        requireId(id, "menuItemId");
        if(menuItemRepository.findById(id).isEmpty()){
            return false;
        }
        return menuItemRepository.deleteById(id);
    }


}
