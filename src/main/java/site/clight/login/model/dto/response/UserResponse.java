package site.clight.login.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

/**
 * 用户响应类，用于封装用户信息。
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
