package com.sprintlog.sprintlogboot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityCategory {

    //상수
    LECTURE("강의", 60),
    PRACTICE("실습", 60),
    READING("독서", 45);

    //필드
    private final String label;
    private final int reviewThresholdMinutes;

    //method
    public boolean isShortStudy(int minutes) {
        return minutes < reviewThresholdMinutes;
    }

}
