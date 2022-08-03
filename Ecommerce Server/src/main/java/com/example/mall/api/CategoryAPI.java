package com.example.mall.api;

import com.example.mall.api.param.CategoryAddParam;
import com.example.mall.api.vo.IndexCategoryVO;
import com.example.mall.common.Exception;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.config.annotation.TokenToUser;
import com.example.mall.domain.Category;
import com.example.mall.domain.User;
import com.example.mall.service.CategoryService;
import com.example.mall.service.UserService;
import com.example.mall.util.BeanUtil;
import com.example.mall.util.NumberUtil;
import com.example.mall.util.Result;
import com.example.mall.util.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class CategoryAPI {

    private ResultGenerator resultGenerator;
    private CategoryService categoryService;
    private BeanUtil beanUtil;

    @Autowired
    public CategoryAPI(ResultGenerator resultGenerator, CategoryService categoryService, BeanUtil beanUtil) {
        this.resultGenerator = resultGenerator;
        this.categoryService = categoryService;
        this.beanUtil = beanUtil;
    }

    @PostMapping("/categories")
    public Result save(@RequestBody @Valid CategoryAddParam categoryAddParam, @TokenToUser User user) {
        log.info("adminUser:{}", user.toString());
        Category Category = new Category();
        beanUtil.copyProperties(categoryAddParam, Category);
        String result = categoryService.saveCategory(Category);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return resultGenerator.genSuccessResult();
        } else {
            return resultGenerator.genFailResult(result);
        }
    }

    @GetMapping("/categories")
    public Result<List<IndexCategoryVO>> getCategories(){
        List<IndexCategoryVO> categories = categoryService.getCategoriesForIndex();
        if(CollectionUtils.isEmpty(categories)){
            Exception.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return resultGenerator.genSuccessResultWithData(categories);
    }
}
