package com.sprintlog.sprintlogboot.domain;


import lombok.Getter;

import java.io.Serializable;

@Getter
public class PracticeLog extends LearningActivity implements Serializable {

    //이 파일의 클래스 구조가 현재 클래스와 같은지에 대한 버전 키 검사용 필드!
    private static final long serialVersionUID = 1L;

    private static final int MINIMUM_COMPLITION_RATE = 70;
    //필드
    private final int completionRate;

    //ctor
    public PracticeLog(String title, int minutes, Visibility visibility, int completionRate) {
        super(title, minutes, visibility, ActivityCategory.PRACTICE);
        this.completionRate = completionRate;
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
