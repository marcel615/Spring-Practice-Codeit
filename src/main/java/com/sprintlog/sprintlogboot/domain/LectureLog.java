package com.sprintlog.sprintlogboot.domain;


import com.sprintlog.sprintlogboot.policy.Reviewable;
import com.sprintlog.sprintlogboot.policy.Shareable;

import java.io.Serializable;

public class LectureLog extends LearningActivity implements Reviewable, Shareable, Serializable {

    //이 파일의 클래스 구조가 현재 클래스와 같은지에 대한 버전 키 검사용 필드!
    private static final long serialVersionUID = 1L;

    //필드
    private String instructorName;

    //ctor
    public LectureLog(String title, int minutes, Visibility visibility, String instructorName) {
        super(title, minutes, visibility, ActivityCategory.LECTURE);
        this.instructorName = instructorName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    //abstract
    @Override
    public String getActivityType() {
        return "강의";
    }

    @Override
    public String getDetailText() {
        return "강사: " + instructorName;
    }


    //interface
    @Override
    public boolean needsReview() {
        return getCategory().isShortStudy(getMinutes());
    }
    @Override
    public void printReviewTarget() {
        System.out.println("[복습 권장] " + getTitle() + " (" + getMinutes() + "분)");
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
    public void method1(){
        System.out.println("Example~~~~");
    }

}
