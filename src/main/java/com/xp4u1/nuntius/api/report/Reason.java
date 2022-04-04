package com.xp4u1.nuntius.api.report;

public class Reason {
    private final String title;
    private final String description;

    public Reason(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
