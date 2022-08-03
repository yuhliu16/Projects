package com.example.mall.config.handler;

import com.example.mall.common.Constants;
import com.example.mall.common.ServiceException;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.config.annotation.TokenToUser;
import com.example.mall.domain.User;
import com.example.mall.domain.UserToken;
import com.example.mall.repository.UserRepository;
import com.example.mall.repository.UserTokenRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("token");
        if(StringUtils.isEmpty(token) || token.length() != Constants.TOKEN_LENGTH){
            ServiceException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
        }
        UserToken userToken = userTokenRepository.findUserTokenByToken(token);
        if(userToken == null || userToken.getExpireTime().getTime() <= System.currentTimeMillis()){
            ServiceException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
        }
        User userFoundByToken = userRepository.findUserByUserId(userToken.getUserId());
        if(userFoundByToken == null){
            ServiceException.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
        }
        return userFoundByToken;
    }
}
