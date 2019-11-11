package com.skb.course.apis.libraryapis.user;

public enum Role {


    ADMIN("Admin"),
    USER("User");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
