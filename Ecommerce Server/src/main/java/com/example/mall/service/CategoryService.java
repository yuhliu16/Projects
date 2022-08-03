package com.example.mall.service;

import com.example.mall.api.vo.IndexCategoryVO;
import com.example.mall.domain.Category;

import java.util.List;

public interface CategoryService {

    List<IndexCategoryVO> getCategoriesForIndex();

    String saveCategory(Category category);

}
