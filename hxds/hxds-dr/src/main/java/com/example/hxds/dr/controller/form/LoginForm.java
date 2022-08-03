package com.example.hxds.dr.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "司机登录表单")
public class LoginForm {
    @NotBlank
    @Schema(description = "微信小程序临时授权")
    private String code;
}
