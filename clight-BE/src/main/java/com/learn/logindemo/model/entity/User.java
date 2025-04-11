package com.learn.logindemo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Set;

/**
 * 用户实体类
 * 该类用于表示用户信息，并映射到 MongoDB 的 "users" 集合。
 * 包含用户的基本信息、角色、状态以及创建和更新时间戳。
 */
@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private org.bson.types.ObjectId id;

    @Field
    @Indexed(unique = true)
    private String username;

    @Field
    private String email;

    @Field
    @JsonIgnore
    private String password;

    @Field(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Field(name = "update_at")
    @LastModifiedDate
    private Date updatedAt;

    @Field
    private Set<String> roles;

    @Field
    private Integer status = 1;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }

    /**
     * 获取用户状态的文本描述
     * 根据用户状态码返回对应的文本描述。
     * @return 用户状态的文本描述
     */
    public String getStatusText() {
        return status == 1 ? "正常" : "封禁";
    }
}
