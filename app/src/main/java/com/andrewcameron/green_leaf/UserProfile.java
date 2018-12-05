package com.andrewcameron.green_leaf;

public class UserProfile {
    private String email, firstName, lastName, organisation;
    private Long numberOfLeaves;

    public UserProfile(){
    }

    public UserProfile(String email, String firstName, String lastName, String organisation, Long numberOfLeaves) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organisation = organisation;
        this.numberOfLeaves = numberOfLeaves;
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

    public Long getNumberOfLeaves() {
        return numberOfLeaves;
    }

    public void setNumberOfLeaves(Long numberOfLeaves) {
        this.numberOfLeaves = numberOfLeaves;
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
