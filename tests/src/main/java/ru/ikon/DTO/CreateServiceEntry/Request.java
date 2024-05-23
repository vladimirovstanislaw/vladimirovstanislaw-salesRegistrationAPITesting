package ru.ikon.DTO.CreateServiceEntry;

public class Request {
    private long consumerID;
    private String tyreSetID;
    private String entryType;
    private String status;

    public Request() {
    }

    public Request(long consumerID, String tyreSetID, String entryType, String status) {
        this.consumerID = consumerID;
        this.tyreSetID = tyreSetID;
        this.entryType = entryType;
        this.status = status;
    }

    public long getConsumerID() {
        return consumerID;
    }

    public void setConsumerID(long consumerID) {
        this.consumerID = consumerID;
    }

    public String getTyreSetID() {
        return tyreSetID;
    }

    public void setTyreSetID(String tyreSetID) {
        this.tyreSetID = tyreSetID;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request{" +
                "consumerID=" + consumerID +
                ", tyreSetID='" + tyreSetID + '\'' +
                ", entryType='" + entryType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
