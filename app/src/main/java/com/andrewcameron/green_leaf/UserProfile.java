package com.andrewcameron.green_leaf;

public class UserProfile {
    private String email, firstName, lastName, organisation;
    private Long currentNumberOfLeaves, totalNumberOfLeaves, plants;

    public UserProfile(){
    }

    public UserProfile(String email, String firstName, String lastName, String organisation, Long currentNumberOfLeaves, Long totalNumberOfLeaves, Long plants) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.currentNumberOfLeaves = currentNumberOfLeaves;
        this.totalNumberOfLeaves = totalNumberOfLeaves;
        this.plants = plants;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public Long getCurrentNumberOfLeaves() {
        return currentNumberOfLeaves;
    }

    public void setCurrentNumberOfLeaves(Long currentNumberOfLeaves) {
        this.currentNumberOfLeaves = currentNumberOfLeaves;
    }

    public Long getTotalNumberOfLeaves() {
        return totalNumberOfLeaves;
    }

    public void setTotalNumberOfLeaves(Long totalNumberOfLeaves) {
        this.totalNumberOfLeaves = totalNumberOfLeaves;
    }

    public Long getPlants() {
        return plants;
    }

    public void setPlants(Long plants) {
        this.plants = plants;
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
}
