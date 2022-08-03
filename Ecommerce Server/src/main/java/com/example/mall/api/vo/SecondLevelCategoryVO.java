package com.example.mall.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class SecondLevelCategoryVO {

    private Long categoryId;

    private Long parentId;

    private Byte categoryLevel;

    private String categoryName;

    private List<ThirdLevelCategoryVO> thirdLevelCategoryVOS;
}
