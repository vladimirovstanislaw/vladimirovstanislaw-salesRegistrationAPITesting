package ru.ikon.DTO.QueryCampaigns;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Campaigns implements Serializable {
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Campaign> Campaign = new ArrayList<>();

    public Campaigns() {
    }

    public Campaigns(List<ru.ikon.DTO.QueryCampaigns.Campaign> campaign) {
        Campaign = campaign;

    }

    public List<ru.ikon.DTO.QueryCampaigns.Campaign> getCampaign() {
        return Campaign;
    }

    public void setCampaign(List<ru.ikon.DTO.QueryCampaigns.Campaign> campaign) {
        Campaign = campaign;

    }

    @Override
    public String toString() {
        return "Campaigns{" +
                "Campaign=" + Campaign +
                '}';
    }
}