package site.clight.login.mongo;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import site.clight.login.LoginDemoApplicationTests;
import site.clight.login.mapper.UserMapper;
import site.clight.login.model.dto.response.UserResponse;
import site.clight.login.model.entity.User;

import java.util.Date;
import java.util.Set;

public class OperateTest extends LoginDemoApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserMapper userMapper;

    @Value("${md5.salt}")
    private String salt;

    // 创建 users 空集合
    @Test
    public void createCollection() {
        if (mongoTemplate.collectionExists("users")) {
            mongoTemplate.dropCollection("users");
        }
        mongoTemplate.createCollection("users");
    }

    // 插入 admin 用户
    @Test
    public void insertUser() {
        String password = DigestUtils.md5Hex("123456" + salt);

        // 创建用户对象
        User user = new User();
        user.setUsername("admin");
        user.setEmail("1357911@gmail.com");
        user.setPassword(password);
        user.setRoles(Set.of("ADMIN", "NORMAL"));
        user.setStatus(1);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // 插入用户
        userMapper.insertUser(user);

        // 验证插入是否成功
        UserResponse insertedUser = userMapper.findByUsername("admin");
        System.out.println("Inserted User: " + insertedUser);
    }
}
