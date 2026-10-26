package repository;

import model.MenuItemModel;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {

    void save(MenuItemModel menu);
    Optional<MenuItemModel> findById(Long id);
    List<MenuItemModel> findAll();
    List<MenuItemModel> findByCategoryId(Long categoryId);
}
