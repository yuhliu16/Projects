package com.example.mall.service.impl;

import com.example.mall.api.param.UserUpdateParam;
import com.example.mall.common.Constants;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.domain.User;
import com.example.mall.domain.UserToken;
import com.example.mall.repository.UserRepository;
import com.example.mall.repository.UserTokenRepository;
import com.example.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private static final Long DEFAULT_EXPIRATION_GAP_IN_MILLISECONDS = 60L * 60 * 24 * 2 * 1000;
    private UserRepository userRepository;
    private UserTokenRepository userTokenRepository;
    private Date now;
    private UserToken userToken;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserTokenRepository userTokenRepository) {
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    public String register(String loginName, String password) {
        if (userRepository.findUserByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        User userToRegister = new User();
        userToRegister.setLoginName(loginName);
        userToRegister.setNickName(loginName);
        userToRegister.setIntroduceSign(Constants.USER_INTRO);
        userToRegister.setPassword(password);

        if (userRepository.save(userToRegister) != null) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String password) {
        User userToLogin = userRepository.findUserByLoginNameAndPassword(loginName, password);
        if (userToLogin == null) {
            return ServiceResultEnum.LOGIN_ERROR.getResult();
        }
        String token = "01234567890123456789012345678901";
        UserToken userToken = userTokenRepository.findUserTokenByUserId(userToLogin.getUserId());
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + DEFAULT_EXPIRATION_GAP_IN_MILLISECONDS);
        if (userToken == null) {
            userToken = new UserToken();
            userToken.setUserId(userToLogin.getUserId());
        }
        userToken.setToken(token);
        userToken.setUpdateTime(now);
        userToken.setExpireTime(expireTime);
        if (userTokenRepository.save(userToken) != null) {
            return token;
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public Boolean updateUserInfo(UserUpdateParam userUpdateParam, Long userId) {
        User userToUpdate = userRepository.findUserByUserId(userId);
        if(userToUpdate == null){
            return false;
        }
        if(userUpdateParam.getNickName() != null){
            userToUpdate.setNickName(userUpdateParam.getNickName());
        }
        if(userUpdateParam.getIntroduceSign() != null){
            userToUpdate.setIntroduceSign(userUpdateParam.getIntroduceSign());
        }
        if(userUpdateParam.getPassword() != null){
            userToUpdate.setPassword(userUpdateParam.getPassword());
        }
        if(userRepository.save(userToUpdate)==null){
            return false;
        }
        return true;
    }

    @Override
    public Boolean logout(Long userId) {
        if(userTokenRepository.findUserTokenByUserId(userId) == null){
            return false;
        }
        userTokenRepository.deleteById(userId);
        return true;
    }
}
