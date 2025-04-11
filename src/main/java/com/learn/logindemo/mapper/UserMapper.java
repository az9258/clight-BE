package com.learn.logindemo.mapper;

import com.learn.logindemo.model.dto.response.UserResponse;
import com.learn.logindemo.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 提供用于操作 MongoDB 的 users 表的方法
 */
@Repository
public interface UserMapper {

    /**
     * 根据用户名查询
     * @param username username
     * @return User
     */
    UserResponse findByUsername(String username);

    /**
     * 根据用户名和密码查询
     * @param username username
     * @param password password
     * @return UserResponse
     */
    UserResponse findUser(String username, String password);

    /**
     * 查询所有用户
     * @return List<UserResponse>
     */
    List<UserResponse> findAll();

    /**
     * 插入用户
     * @param user User
     */
    void insertUser(User user);

    /**
     * 更新用户
     * @param user User
     * @return boolean
     */
    boolean updateUser(User user);

    /**
     * 根据用户名删除用户
     * @param username username
     */
    void deleteByUsername(String username);
}
