package site.clight.login.mapper;

import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户Mapper接口
 * 提供 users 表的增删改查方法
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
     * @return User
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
