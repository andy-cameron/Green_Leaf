package com.andrewcameron.green_leaf;

public class UserPreferences {
    private Boolean presentMonday, presentTuesday, presentWednesday, presentThursday, presentFriday;

    public UserPreferences(Boolean presentMonday, Boolean presentTuesday, Boolean presentWednesday, Boolean presentThursday, Boolean presentFriday) {
        this.presentMonday = presentMonday;
        this.presentTuesday = presentTuesday;
        this.presentWednesday = presentWednesday;
        this.presentThursday = presentThursday;
        this.presentFriday = presentFriday;
    }

    public Boolean getPresentMonday() {
        return presentMonday;
    }

    public void setPresentMonday(Boolean presentMonday) {
        this.presentMonday = presentMonday;
    }

    public Boolean getPresentTuesday() {
        return presentTuesday;
    }

    public void setPresentTuesday(Boolean presentTuesday) {
        this.presentTuesday = presentTuesday;
    }

    public Boolean getPresentWednesday() {
        return presentWednesday;
    }

    public void setPresentWednesday(Boolean presentWednesday) {
        this.presentWednesday = presentWednesday;
    }

    public Boolean getPresentThursday() {
        return presentThursday;
    }

    public void setPresentThursday(Boolean presentThursday) {
        this.presentThursday = presentThursday;
    }

    public Boolean getPresentFriday() {
        return presentFriday;
    }

    public void setPresentFriday(Boolean presentFriday) {
        this.presentFriday = presentFriday;
    }
}
