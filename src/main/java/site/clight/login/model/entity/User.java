package site.clight.login.model.entity;

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

    public String getStatusText() {
        return status == 1 ? "正常" : "封禁";
    }
}
