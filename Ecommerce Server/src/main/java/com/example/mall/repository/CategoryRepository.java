package com.example.mall.repository;

import com.example.mall.domain.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Category save(Category category);

    Category findCategoryByCategoryId(Long categoryId);

    List<Category> findByParentIdInAndCategoryLevel(List<Long> parentIds, int categoryLevel, Pageable pageable);

    Category findCategoryByCategoryLevelAndCategoryName(int categoryLevel, String categoryName);

    void deleteById(Long categoryId);
}
