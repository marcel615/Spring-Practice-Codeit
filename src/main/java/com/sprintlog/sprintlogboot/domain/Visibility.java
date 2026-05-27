package com.sprintlog.sprintlogboot.domain;

public enum Visibility {

    //상수
    PUBLIC("공개", true),
    PRIVATE("비공개", false);
    //FRIENDS_ONLY;

    //필드
    private final String label;
    private final boolean shareable;

    //ctor
    Visibility(String label, boolean shareable){
        this.label = label;
        this.shareable = shareable;
    }

    //getter
    public String getLabel() {
        return label;
    }
    public boolean isShareable() {
        return shareable;
    }

}
