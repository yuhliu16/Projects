package com.example.mall.api;

import com.example.mall.api.param.UserLoginParam;
import com.example.mall.api.param.UserRegisterParam;
import com.example.mall.api.param.UserUpdateParam;
import com.example.mall.api.vo.UserVO;
import com.example.mall.common.Constants;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.config.annotation.TokenToUser;
import com.example.mall.domain.User;
import com.example.mall.service.UserService;
import com.example.mall.util.BeanUtil;
import com.example.mall.util.NumberUtil;
import com.example.mall.util.Result;
import com.example.mall.util.ResultGenerator;
//import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserAPI {

    private NumberUtil numberUtil;
    private ResultGenerator resultGenerator;
    private UserService userService;
    private BeanUtil beanUtil;

    @Autowired
    public UserAPI(NumberUtil numberUtil, ResultGenerator resultGenerator, UserService userService, BeanUtil beanUtil) {
        this.numberUtil = numberUtil;
        this.resultGenerator = resultGenerator;
        this.userService = userService;
        this.beanUtil = beanUtil;
    }

    @PostMapping("/user/login")
    public Result<String> login(@RequestBody @Valid UserLoginParam param){
        if(numberUtil.isNotPhoneNumber(param.getLoginName())) {
            return resultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String loginResult = userService.login(param.getLoginName(), param.getPassword());

        log.info("login api, loginName={}, loginResult={}", param.getLoginName(), loginResult);

        if(!StringUtils.isEmpty(loginResult) && loginResult.length() == Constants.TOKEN_LENGTH){
            return resultGenerator.genSuccessResultWithData(loginResult);
        }
        return resultGenerator.genFailResult(loginResult);
    }

    @PostMapping("/user/logout")
    public Result<String> logout(@TokenToUser User loginUser){
        boolean logoutResult = userService.logout(loginUser.getUserId());

        log.info("logout api, loginMallUser={}", loginUser.getUserId());
        if(logoutResult){
            return resultGenerator.genSuccessResult();
        }
        return resultGenerator.genFailResult(ServiceResultEnum.LOGOUT_ERROR.getResult());
    }

    @PostMapping("/user/register")
//    @ApiOperation(value = "用户注册", notes = "")
    public Result register(@RequestBody @Valid UserRegisterParam param){
        if(numberUtil.isNotPhoneNumber(param.getLoginName())){
            return resultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String registerResult = userService.register(param.getLoginName(), param.getPassword());

        log.info("register api, loginName={}, registerResult={}", param.getLoginName(), registerResult);
        if(ServiceResultEnum.SUCCESS.getResult().equals(registerResult)){
            return resultGenerator.genSuccessResult();
        }
        return resultGenerator.genFailResult(registerResult);
    }

    @PutMapping("/user/info")
    public Result updateUserDetail(@TokenToUser User loginUser, @RequestBody UserUpdateParam userUpdateParam){
        if(!userService.updateUserInfo(userUpdateParam, loginUser.getUserId())){
            return resultGenerator.genFailResult(ServiceResultEnum.USER_UPDATE_ERROR.getResult());
        }
        return resultGenerator.genSuccessResult();
    }

    @GetMapping("/user/info")
    public Result<UserVO> getUserDetail(@TokenToUser User loginUser){
        UserVO userVO = new UserVO();
        beanUtil.copyProperties(loginUser, userVO);
        return resultGenerator.genSuccessResultWithData(userVO);
    }
}
