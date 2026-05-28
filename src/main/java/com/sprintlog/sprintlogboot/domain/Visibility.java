package com.sprintlog.sprintlogboot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Visibility {

    //상수
    PUBLIC("공개", true),
    PRIVATE("비공개", false);
    //FRIENDS_ONLY;

    //필드
    private final String label;
    private final boolean shareable;

}
