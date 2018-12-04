package com.andrewcameron.green_leaf;

public class UserProfile {
    private String email, name, organisation;
    private Long numberOfLeaves;

    public UserProfile(String email, String name, String organisation, Long numberOfLeaves) {
        this.email = email;
        this.name = name;
        this.organisation = organisation;
        this.numberOfLeaves = numberOfLeaves;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public Long getNumberOfLeaves() {
        return numberOfLeaves;
    }

    public void setNumberOfLeaves(Long numberOfLeaves) {
        this.numberOfLeaves = numberOfLeaves;
    }
}
