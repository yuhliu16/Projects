package com.example.mall.service.impl;

import com.example.mall.api.vo.IndexCategoryVO;
import com.example.mall.api.vo.SecondLevelCategoryVO;
import com.example.mall.api.vo.ThirdLevelCategoryVO;
import com.example.mall.common.CategoryLevelEnum;
import com.example.mall.common.Constants;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.domain.Category;
import com.example.mall.repository.CategoryRepository;
import com.example.mall.repository.UserRepository;
import com.example.mall.repository.UserTokenRepository;
import com.example.mall.service.CategoryService;
import com.example.mall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private BeanUtil beanUtil;
    private List<Category> secondLevelCategories;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, BeanUtil beanUtil) {
        this.categoryRepository = categoryRepository;
        this.beanUtil = beanUtil;
    }

    @Override
    public String saveCategory(Category category) {
        if(categoryRepository.findCategoryByCategoryLevelAndCategoryName(category.getCategoryLevel(), category.getCategoryName()) != null){
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        if(categoryRepository.save(category) == null){
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.SUCCESS.getResult();
    }

    @Override
    public List<IndexCategoryVO> getCategoriesForIndex() {
        List<IndexCategoryVO> indexCategoryVOS = new ArrayList<>();
        List<Category> firstLevelCategories = categoryRepository.findByParentIdInAndCategoryLevel(new ArrayList<>() {{add(0L);}}, CategoryLevelEnum.LEVEL_ONE.getLevel(), PageRequest.of(0, Constants.INDEX_CATEGORY_NUMBER, Sort.by("categoryRank")));
        if (CollectionUtils.isEmpty(firstLevelCategories)) {
            return null;
        }
        List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(Category::getCategoryId).collect(Collectors.toList());
        List<Category> secondLevelCategories = categoryRepository.findByParentIdInAndCategoryLevel(firstLevelCategoryIds, CategoryLevelEnum.LEVEL_TWO.getLevel(), PageRequest.of(0,100000));
        if (!CollectionUtils.isEmpty(secondLevelCategories)) {
            List<Long> secondLevelCategoryIds = secondLevelCategories.stream().map(Category::getCategoryId).collect(Collectors.toList());
            List<Category> thirdLevelCategories = categoryRepository.findByParentIdInAndCategoryLevel(secondLevelCategoryIds, CategoryLevelEnum.LEVEL_THREE.getLevel(), PageRequest.of(0,100000));
            if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
                Map<Long, List<Category>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(Category::getParentId));
                List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
                for (Category secondLevelCategory : secondLevelCategories) {
                    SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
                    beanUtil.copyProperties(secondLevelCategory, secondLevelCategoryVO);
                    if (thirdLevelCategoryMap.containsKey(secondLevelCategory.getCategoryId())) {
                        List<Category> tempCategories = thirdLevelCategoryMap.get(secondLevelCategory.getCategoryId());
                        secondLevelCategoryVO.setThirdLevelCategoryVOS((beanUtil.copyList(tempCategories, ThirdLevelCategoryVO.class)));
                        secondLevelCategoryVOS.add(secondLevelCategoryVO);
                    }
                }
                if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
                    Map<Long, List<SecondLevelCategoryVO>> secondLevelCategoryVOMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
                    for (Category firstCategory : firstLevelCategories) {
                        IndexCategoryVO indexCategoryVO = new IndexCategoryVO();
                        beanUtil.copyProperties(firstCategory, indexCategoryVO);
                        if (secondLevelCategoryVOMap.containsKey(firstCategory.getCategoryId())) {
                            List<SecondLevelCategoryVO> tempGoodsCategories = secondLevelCategoryVOMap.get(firstCategory.getCategoryId());
                            indexCategoryVO.setSecondLevelCategoryVOS(tempGoodsCategories);
                            indexCategoryVOS.add(indexCategoryVO);
                        }
                    }
                }
            }
        }
        return indexCategoryVOS;
    }

}
