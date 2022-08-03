package com.example.hxds.bff.driver.feign;

import com.example.hxds.bff.driver.controller.form.CreateDriverFaceModelForm;
import com.example.hxds.bff.driver.controller.form.LoginForm;
import com.example.hxds.bff.driver.controller.form.RegisterNewDriverForm;
import com.example.hxds.bff.driver.controller.form.SearchDriverAuthForm;
import com.example.hxds.bff.driver.controller.form.SearchDriverBaseInfoForm;
import com.example.hxds.bff.driver.controller.form.SearchDriverSettingsForm;
import com.example.hxds.bff.driver.controller.form.UpdateDriverAuthForm;
import com.example.hxds.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "hxds-dr")
public interface DrServiceApi {

    @PostMapping("/driver/registerNewDriver")
    public R registerNewDriver(RegisterNewDriverForm form);

    @PostMapping("/driver/updateDriverAuth")
    public R updateDriverAuth(UpdateDriverAuthForm form);

    @PostMapping("/driver/createDriverFaceModel")
    public R createDriverFaceModel(CreateDriverFaceModelForm form);

    @PostMapping("/driver/login")
    public R login(LoginForm form);

    @PostMapping("/driver/searchDriverBaseInfo")
    public R searchDriverBaseInfo(SearchDriverBaseInfoForm form);

    @PostMapping("/settings/searchDriverSettings")
    public R searchDriverSettings(SearchDriverSettingsForm form);

    @PostMapping("/driver/searchDriverAuth")
    public R searchDriverAuth(SearchDriverAuthForm form);
}
