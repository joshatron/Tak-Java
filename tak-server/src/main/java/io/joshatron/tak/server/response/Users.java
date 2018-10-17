package io.joshatron.tak.server.response;

public class Users {

    private String[] users;

    public Users(String[] users) {
        this.users = users;
    }

    public Users() {
        users = null;
    }

    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }
}
