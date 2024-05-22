package ru.ikon.DTO.QueryCampaigns;

import java.io.Serializable;

public class Campaign implements Serializable {
    private long campaignID;
    private String campaignType;
    private String serviceCode;

    public Campaign() {
    }

    public Campaign(long campaignID, String campaignType, String serviceCode) {
        this.campaignID = campaignID;
        this.campaignType = campaignType;
        this.serviceCode = serviceCode;
    }

    public long getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(long campaignID) {
        this.campaignID = campaignID;
    }

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "campaignID=" + campaignID +
                ", campaignType='" + campaignType + '\'' +
                ", serviceCode='" + serviceCode + '\'' +
                '}';
    }
}
