package ru.ikon.entities;

public class IntegrationPartner {
    private String login;
    private String password;
    private String PosCode;

    public IntegrationPartner() {
    }

    public IntegrationPartner(String login, String password, String posCode) {
        this.login = login;
        this.password = password;
        PosCode = posCode;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPosCode() {
        return PosCode;
    }

    public void setPosCode(String posCode) {
        PosCode = posCode;
    }

    @Override
    public String toString() {
        return "IntegrationPartner{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", PosCode='" + PosCode + '\'' +
                '}';
    }
}
