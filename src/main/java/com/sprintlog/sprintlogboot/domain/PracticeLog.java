package com.sprintlog.sprintlogboot.domain;


import com.sprintlog.sprintlogboot.policy.Reviewable;
import com.sprintlog.sprintlogboot.policy.Shareable;

import java.io.Serializable;

public class PracticeLog extends LearningActivity implements Reviewable, Shareable, Serializable {

    //이 파일의 클래스 구조가 현재 클래스와 같은지에 대한 버전 키 검사용 필드!
    private static final long serialVersionUID = 1L;

    private static final int MINIMUM_COMPLITION_RATE = 70;
    //필드
    private int completionRate;

    //ctor
    public PracticeLog(String title, int minutes, Visibility visibility, int completionRate) {
        super(title, minutes, visibility, ActivityCategory.PRACTICE);
        this.completionRate = completionRate;
    }

    //getter, setter
    public int getCompletionRate() {
        return completionRate;
    }
    public void setCompletionRate(int completionRate) {
        this.completionRate = normalizeCompletionRate(completionRate);
    }


    //abstract
    @Override
    public String getActivityType() {
        return "실습";
    }

    @Override
    public String getDetailText() {
        return "완료율: " + completionRate + "%";
    }

    //interface
    @Override
    public boolean needsReview() {
        return getCategory().isShortStudy(getMinutes());
    }
    @Override
    public void printReviewTarget() {
        System.out.println("[복습 권장] " + getTitle() + " (완료율"  + completionRate + "%)");
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
    private int normalizeCompletionRate(int completionRate) {
        if (completionRate < 0){
            return 0;
        }
        if (completionRate > 100){
            return 100;
        }

        return completionRate;
    }

}
