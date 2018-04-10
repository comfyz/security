package xyz.comfyz.security.example.bo;

import java.util.Date;

/**
 * @author : comfy create at 2018-04-10 09:58
 */
public class User {
    private String userId;
    private String name;
    private Date birth;

    public String getUserId() {
        return userId;
    }

    public User setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public Date getBirth() {
        return birth;
    }

    public User setBirth(Date birth) {
        this.birth = birth;
        return this;
    }
}
