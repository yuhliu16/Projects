package com.example.hxds.bff.driver.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "NewDriverRegisterForm")
public class RegisterNewDriverForm {
    @NotBlank(message = "code cannot be blank")
    @Schema(description = "微信小程序临时授权")
    private String code;

    @NotBlank(message = "nickname cannot be blank")
    @Schema(description = "用户昵称")
    private String nickname;

    @NotBlank(message = "photo cannot be blank")
    @Schema(description = "用户头像")
    private String photo;
}