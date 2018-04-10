package xyz.comfyz.security.model;

public class UserDetalis {

    private final String userId;
    private final String userName;
    private final boolean admin;

    public UserDetalis(String userId, String userName, boolean admin) {
        this.userId = userId;
        this.userName = userName;
        this.admin = admin;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isAdmin() {
        return admin;
    }
}
