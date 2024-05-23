package ru.ikon.DTO.CreateServiceEntry;

public class ServiceEntry {
    private String serviceEntryID;
    private String serviceCode;
    private String serviceTerms;
    private String serviceInstructions;
    private String validFrom;
    private String validTo;

    public ServiceEntry() {
    }

    public ServiceEntry(String serviceEntryID, String serviceCode, String serviceTerms, String serviceInstructions, String validFrom, String validTo) {
        this.serviceEntryID = serviceEntryID;
        this.serviceCode = serviceCode;
        this.serviceTerms = serviceTerms;
        this.serviceInstructions = serviceInstructions;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getServiceEntryID() {
        return serviceEntryID;
    }

    public void setServiceEntryID(String serviceEntryID) {
        this.serviceEntryID = serviceEntryID;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceTerms() {
        return serviceTerms;
    }

    public void setServiceTerms(String serviceTerms) {
        this.serviceTerms = serviceTerms;
    }

    public String getServiceInstructions() {
        return serviceInstructions;
    }

    public void setServiceInstructions(String serviceInstructions) {
        this.serviceInstructions = serviceInstructions;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    @Override
    public String toString() {
        return "ServiceEntry{" +
                "serviceEntryID='" + serviceEntryID + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                ", serviceTerms='" + serviceTerms + '\'' +
                ", serviceInstructions='" + serviceInstructions + '\'' +
                ", validFrom='" + validFrom + '\'' +
                ", validTo='" + validTo + '\'' +
                '}';
    }
}
