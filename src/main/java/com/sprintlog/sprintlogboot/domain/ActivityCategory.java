package com.sprintlog.sprintlogboot.domain;

public enum ActivityCategory {

    //상수
    LECTURE("강의", 60),
    PRACTICE("실습", 60),
    READING("독서", 45);

    //필드
    private final String label;
    private final int reviewThresholdMinutes;

    //ctor
    ActivityCategory(String label, int reviewThresholdMinutes) {
        this.label = label;
        this.reviewThresholdMinutes = reviewThresholdMinutes;
    }

    //getter
    public String getLabel() {
        return label;
    }

    //method
    public boolean isShortStudy(int minutes) {
        return minutes < reviewThresholdMinutes;
    }

}
