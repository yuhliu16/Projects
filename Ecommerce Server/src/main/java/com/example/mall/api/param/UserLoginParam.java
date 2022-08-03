package com.example.mall.api.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginParam {

    //    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String loginName;

    //    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
