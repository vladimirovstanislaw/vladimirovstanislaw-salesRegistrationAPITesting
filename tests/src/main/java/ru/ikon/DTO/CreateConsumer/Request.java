package ru.ikon.DTO.CreateConsumer;

public class Request {
    private String phone;
    private String lastName;

    public Request() {
    }

    public Request(String phone, String lastName) {
        this.phone = "+" + phone;
        this.lastName = "+" + lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = "+" + phone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = "+" + lastName;
    }

    @Override
    public String toString() {
        return "Request{" +
                "phone='" + phone + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
