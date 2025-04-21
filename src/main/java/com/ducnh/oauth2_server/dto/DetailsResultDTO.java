package com.ducnh.oauth2_server.dto;

import lombok.Data;

@Data
public class DetailsResultDTO {
    private Long athleteId;
    private int teamId;
    private String teamName;
    private String athleteName;
    private Double totalDistance;
    private Double totalCurrentDistance;
    private int stt;
    private Double totalDistanceEx;

    public String formatTotalDistance() {
        return String.format("%.1f", this.totalDistance / 1000);
    }

    public String formatTotalCurrentDistance() {
        return String.format("%.2f", this.totalCurrentDistance / 1000);
    }

    public String formatTotalDistanceEx() {
        return String.format("%.1f", this.totalDistanceEx / 1000);
    }

}
