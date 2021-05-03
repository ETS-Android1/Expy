package com.xdev.expy.ui.onboarding;

public class Onboarding {

    private final int imageRes;
    private final String title;
    private final String description;

    public Onboarding(int imageRes, String title, String description) {
        this.imageRes = imageRes;
        this.title = title;
        this.description = description;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
