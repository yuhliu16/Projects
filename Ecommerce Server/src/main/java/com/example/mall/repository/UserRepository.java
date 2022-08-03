package com.example.mall.repository;

import com.example.mall.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByLoginName(String loginName);

    User save(User user);

    User findUserByLoginNameAndPassword(String loginName, String password);

    User findUserByUserId(Long userId);
}
