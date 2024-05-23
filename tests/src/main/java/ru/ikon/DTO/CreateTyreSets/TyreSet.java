package ru.ikon.DTO.CreateTyreSets;

public class TyreSet {
    private String vehicleID;
    private String tyreSetID;
    private String itemCode;
    private String tyreCount;
    private String purchaseDate;

    public TyreSet() {
    }

    public TyreSet(String vehicleID, String tyreSetID, String itemCode, String tyreCount, String purchaseDate) {
        this.vehicleID = vehicleID;
        this.tyreSetID = tyreSetID;
        this.itemCode = itemCode;
        this.tyreCount = tyreCount;
        this.purchaseDate = purchaseDate;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getTyreSetID() {
        return tyreSetID;
    }

    public void setTyreSetID(String tyreSetID) {
        this.tyreSetID = tyreSetID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getTyreCount() {
        return tyreCount;
    }

    public void setTyreCount(String tyreCount) {
        this.tyreCount = tyreCount;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "TyreSet{" +
                "vehicleID='" + vehicleID + '\'' +
                ", tyreSetID='" + tyreSetID + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", tyreCount='" + tyreCount + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                '}';
    }
}
