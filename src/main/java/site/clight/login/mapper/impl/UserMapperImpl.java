package site.clight.login.mapper.impl;

import site.clight.login.mapper.UserMapper;
import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserMapperImpl implements UserMapper {

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    @Override
    public UserResponse findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) {
            return null;
        }
        return convertToUserResponse(user);
    }

    @Override
    public UserResponse findUser(String username, String password) {
        Query query = new Query(Criteria.where("username").is(username));
        query.addCriteria(Criteria.where("password").is(password));
        User user = mongoTemplate.findOne(query, User.class);
        if (user == null) {
            return null;
        }
        return convertToUserResponse(user);
    }

    /**
     * 查询所有用户
     * @return 用户响应对象列表
     */
    @Override
    public List<UserResponse> findAll() {
        List<User> users = mongoTemplate.findAll(User.class);
        return users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * 插入用户
     *
     * @param user 用户对象
     */
    @Override
    public void insertUser(User user) {
        mongoTemplate.insert(user, "users");
    }

    /**
     * 更新用户数据
     * @param user 用户对象
     * @return 更新结果
     */
    @Override
    public boolean updateUser(User user) {
        Query query = new Query(Criteria.where("username").is(user.getUsername()));
        Update update = new Update();
        if (user.getEmail() != null) {
            update.set("email", user.getEmail());
        }
        if (user.getRoles() != null) {
            update.set("roles", user.getRoles());
        }
        if (user.getStatus() != null) {
            update.set("status", user.getStatus());
        }
        if (user.getPassword() != null) {
            update.set("password", user.getPassword());
        }
        return mongoTemplate.updateFirst(query, update, User.class).wasAcknowledged();
    }

    @Override
    public void deleteByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        mongoTemplate.remove(query, User.class);
    }

    /**
     * 将用户实体转换为用户响应对象
     * @param user 用户实体
     * @return 用户响应对象
     */
    private UserResponse convertToUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRoles(), user.getStatus());
    }
}
