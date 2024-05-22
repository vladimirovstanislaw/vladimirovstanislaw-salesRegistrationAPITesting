package ru.ikon.DTO.QueryConsumers;

public class Consumer {
    private long consumerID;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String addressStreet;
    private String addressPostalCode;
    private String addressCity;
    private String addressCountry;

    private transient String vehicles;
    private transient String tyresets;


    //private transient String Vehicles;
    //private transient String Tyresets;

    public Consumer() {
    }

    public Consumer(long consumerID, String email, String phone, String firstName, String lastName, String addressStreet, String addressPostalCode, String addressCity, String addressCountry, String vehicles, String tyresets) {
        this.consumerID = consumerID;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressStreet = addressStreet;
        this.addressPostalCode = addressPostalCode;
        this.addressCity = addressCity;
        this.addressCountry = addressCountry;
        this.vehicles = vehicles;
        this.tyresets = tyresets;
    }

    public long getConsumerID() {
        return consumerID;
    }

    public void setConsumerID(long consumerID) {
        this.consumerID = consumerID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getVehicles() {
        return vehicles;
    }

    public void setVehicles(String vehicles) {
        this.vehicles = vehicles;
    }

    public String getTyresets() {
        return tyresets;
    }

    public void setTyresets(String tyresets) {
        this.tyresets = tyresets;
    }

    @Override
    public String toString() {
        return "Consumer{" +
                "consumerID=" + consumerID +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                ", addressPostalCode='" + addressPostalCode + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", addressCountry='" + addressCountry + '\'' +
                ", vehicles='" + vehicles + '\'' +
                ", tyresets='" + tyresets + '\'' +
                '}';
    }
}
