package repository;

import model.MenuItemModel;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends CrudRepository<MenuItemModel>{

    List<MenuItemModel> findByCategoryId(Long categoryId);
}
