package com.ducnh.oauth2_server.dto;

public class SummaryEventDTO {
    
    private String teamName;
    private String eventId;
    private int teamId;
    private int teamCount;
    private Double totalDistance;
    private Double totalCurrentDistance;
    private int stt;
    private Double totalDistanceEx;

    public SummaryEventDTO() {}

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
    public String getEventId() {
        return this.eventId;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    public int getTeamCount() {
        return this.teamCount;
    }   

    public void setTotalDistanceEx(Double exRate) {
        this.totalDistanceEx = exRate;
    }

    public String getTotalDistanceEx() {
        return String.format("%.1f", this.totalDistanceEx / 1000);
    }


    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getTeamId() {
        return this.teamId;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalDistance() {
        return String.format("%.1f", this.totalDistance / 1000);
    }

    public String formatTotalDistance() {
        return String.format("%.1f", this.totalDistance / 1000);
    }

    public String formatTotalCurrentDistance() {
        return String.format("%.2f", this.totalCurrentDistance / 1000);
    }

    public void setTotalCurrentDistance(Double currentDistance) {
        this.totalCurrentDistance = currentDistance;
    }

    public String getTotalCurrentDistance() {
        return String.format("%.2f", this.totalCurrentDistance / 1000);
    }

    public void setSTT(int stt) {
        this.stt = stt;
    }

    public int getSTT() {
        return this.stt;
    }
}