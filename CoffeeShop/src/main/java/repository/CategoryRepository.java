package repository;

import model.CategoryModel;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<CategoryModel> findAll();
    Optional<CategoryModel> findById(Long id);
}
