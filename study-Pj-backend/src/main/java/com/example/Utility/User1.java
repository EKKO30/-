package com.example.Utility;

import lombok.Data;

@Data
public class User1 {
    private int id;
    private String email;
    private String username;
    private String password;

    public User1(int id, String email, String username, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User1() {
    }
}
