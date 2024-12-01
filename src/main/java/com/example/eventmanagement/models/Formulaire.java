package com.example.eventmanagement.models;

public class Formulaire {

    private Integer numberOfPeople;
    private Boolean isOpenSpacePreferred;
    private Double budget;
    private String locationPreference;

    // Getters et Setters
    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Boolean getIsOpenSpacePreferred() {
        return isOpenSpacePreferred;
    }

    public void setIsOpenSpacePreferred(Boolean isOpenSpacePreferred) {
        this.isOpenSpacePreferred = isOpenSpacePreferred;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getLocationPreference() {
        return locationPreference;
    }

    public void setLocationPreference(String locationPreference) {
        this.locationPreference = locationPreference;
    }
}
