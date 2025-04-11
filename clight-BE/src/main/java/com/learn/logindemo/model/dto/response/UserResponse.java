package com.learn.logindemo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

/**
 * 用户响应实体类
 * 该类用于封装用户信息的响应数据，包括用户的基本信息和角色信息。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    @Id
    private org.bson.types.ObjectId id;

    @Field
    private String username;

    @Field
    private String email;

    @Field
    private Set<String> roles;

    @Field
    private Integer status;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}
