package com.example.hxds.bff.driver.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import com.example.hxds.bff.driver.controller.form.CreateDriverFaceModelForm;
import com.example.hxds.bff.driver.controller.form.LoginForm;
import com.example.hxds.bff.driver.controller.form.RegisterNewDriverForm;
import com.example.hxds.bff.driver.controller.form.SearchDriverAuthForm;
import com.example.hxds.bff.driver.controller.form.SearchDriverBaseInfoForm;
import com.example.hxds.bff.driver.controller.form.UpdateDriverAuthForm;
import com.example.hxds.bff.driver.service.DriverService;
import com.example.hxds.common.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/driver")
@Tag(name = "DriverController", description = "司机模块Web接口")
public class DriverController {
    @Resource
    private DriverService driverService;

    @PostMapping("/registerNewDriver")
    @Operation(summary = "新司机注册")
    public R registerNewDriver(@RequestBody @Valid RegisterNewDriverForm form) {
        long driverId = driverService.registerNewDriver(form);
        StpUtil.login(driverId);
        String token = StpUtil.getTokenInfo().getTokenValue();
        return R.ok().put("token", token);
    }

    @PostMapping("/updateDriverAuth")
    @Operation(summary = "更新实名认证信息")
    @SaCheckLogin
    public R updateDriverAuth(@RequestBody @Valid UpdateDriverAuthForm form){
        form.setDriverId(StpUtil.getLoginIdAsLong());
        int rows = driverService.updateDriverAuth(form);
        return R.ok().put("rows", rows);
    }

    @PostMapping("/createDriverFaceModel")
    @Operation(summary = "司机人脸注册")
    public R createDriverFaceModel(@RequestBody @Valid CreateDriverFaceModelForm form){
        form.setDriverId(StpUtil.getLoginIdAsLong());
        String result = driverService.createDriverFaceModel(form);
        return R.ok().put("result", result);
    }

    @PostMapping("/login")
    @Operation(summary = "司机登录系统")
    public R login(@RequestBody @Valid LoginForm form){
        HashMap map = driverService.login(form);
        if(map!=null){
            long driverId = MapUtil.getLong(map,"id");
            byte realAuth = Byte.parseByte(MapUtil.getStr(map,"realAuth"));
            boolean archive = MapUtil.getBool(map,"archive");

            StpUtil.login(driverId);
            String token = StpUtil.getTokenInfo().getTokenValue();
            return R.ok().put("token", token).put("realAuth", realAuth).put("archive",archive);
        }
        return R.ok();
    }

    @PostMapping("/searchDriverBaseInfo")
    @Operation(summary = "查找司机基础信息")
    @SaCheckLogin
    public R searchDriverBaseInfo(){
        long driverId = StpUtil.getLoginIdAsLong();
        SearchDriverBaseInfoForm form = new SearchDriverBaseInfoForm();
        form.setDriverId(driverId);
        HashMap map = driverService.searchDriverBaseInfo(form);
        return R.ok().put("result",map);
    }

    @GetMapping("/logout")
    @Operation(summary = "退出系统")
    @SaCheckLogin
    public R logout() {
        StpUtil.logout();
        return R.ok();
    }

    @PostMapping("/searchWorkbenchData")
    @Operation(summary = "查找司机工作台数据")
    @SaCheckLogin
    public R searchWorkbenchData(){
        HashMap result = driverService.searchWorkbenchData(StpUtil.getLoginIdAsLong());
        return R.ok().put("result", result);
    }

    @GetMapping("/searchDriverAuth")
    @Operation(summary = "查询司机认证信息")
    @SaCheckLogin
    public R searchDriverAuth(){
        long driverId = StpUtil.getLoginIdAsLong();
        SearchDriverAuthForm form = new SearchDriverAuthForm();
        form.setDriverId(driverId);
        HashMap map = driverService.searchDriverAuth(form);
        return R.ok().put("result",map);
    }
}

