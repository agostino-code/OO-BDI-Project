package com.unina.project;

public class Autenticazione {

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String email;
    public String password;

}
