package com.example.mall.repository;

import com.example.mall.domain.UserToken;
import org.springframework.data.repository.CrudRepository;

public interface UserTokenRepository extends CrudRepository<UserToken, Long> {

    UserToken save(UserToken userToken);

    UserToken findUserTokenByUserId(Long userId);

    UserToken findUserTokenByToken(String token);

    void deleteById(Long userId);

}
