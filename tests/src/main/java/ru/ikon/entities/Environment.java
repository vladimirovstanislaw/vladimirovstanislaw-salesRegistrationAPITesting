package ru.ikon.entities;

public class Environment {
    private String type;
    private String apiSite;

    public Environment() {
    }

    public Environment(String type, String apiSite) {
        this.type = type;
        this.apiSite = apiSite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApiSite() {
        return apiSite;
    }

    public void setApiSite(String apiSite) {
        this.apiSite = apiSite;
    }

    @Override
    public String toString() {
        return "Environment{" +
                "type='" + type + '\'' +
                ", apiSite='" + apiSite + '\'' +
                '}';
    }
}
