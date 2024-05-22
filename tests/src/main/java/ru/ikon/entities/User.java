package ru.ikon.entities;

public class User {
    private String phone;

    public User() {
    }

    public User(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
