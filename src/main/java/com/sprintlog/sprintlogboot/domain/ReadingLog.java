package com.sprintlog.sprintlogboot.domain;


import com.sprintlog.sprintlogboot.policy.Reviewable;
import com.sprintlog.sprintlogboot.policy.Shareable;

import java.io.Serializable;

public class ReadingLog extends LearningActivity implements Reviewable, Shareable, Serializable {

    //이 파일의 클래스 구조가 현재 클래스와 같은지에 대한 버전 키 검사용 필드!
    private static final long serialVersionUID = 1L;
    //필드
    private String bookTitle;

    //ctor
    public ReadingLog(String title, int minutes, Visibility visibility, String bookTitle) {
        super(title, minutes, visibility, ActivityCategory.READING);
        this.bookTitle = bookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    //abstract
    @Override
    public String getActivityType() {
        return "독서";
    }

    @Override
    public String getDetailText() {
        return "책: " + bookTitle;
    }

    //interface
    @Override
    public boolean needsReview() {
        return false;
    }
    @Override
    public void printReviewTarget() {
        System.out.println("[복습 권장] " + getTitle() + " (" + bookTitle + ")");
    }

    @Override
    public boolean canShare() {
        return getVisibility() == Visibility.PUBLIC;
    }
    @Override
    public String getShareTitle() {
        return getTitle();
    }

    //method

}
