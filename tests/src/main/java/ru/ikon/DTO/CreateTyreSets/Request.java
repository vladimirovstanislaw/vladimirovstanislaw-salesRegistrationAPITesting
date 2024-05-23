package ru.ikon.DTO.CreateTyreSets;

public class Request {
    private long consumerID;
    private String itemCode;
    private int tyreCount;
    private String purchaseDate;

    public Request() {
    }

    public Request(long consumerID, String itemCode, int tyreCount, String purchaseDate) {
        this.consumerID = consumerID;
        this.itemCode = itemCode;
        this.tyreCount = tyreCount;
        this.purchaseDate = purchaseDate;
    }

    public long getConsumerID() {
        return consumerID;
    }

    public void setConsumerID(long consumerID) {
        this.consumerID = consumerID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getTyreCount() {
        return tyreCount;
    }

    public void setTyreCount(int tyreCount) {
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
        return "Request{" +
                "consumerID=" + consumerID +
                ", itemCode='" + itemCode + '\'' +
                ", tyreCount=" + tyreCount +
                ", purchaseDate='" + purchaseDate + '\'' +
                '}';
    }
}
