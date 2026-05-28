package com.sprintlog.sprintlogboot.domain;



import java.io.Serializable;

public class LectureLog extends LearningActivity implements Serializable {

    //이 파일의 클래스 구조가 현재 클래스와 같은지에 대한 버전 키 검사용 필드!
    private static final long serialVersionUID = 1L;

    //필드
    private String instructorName;

    //ctor
    public LectureLog(String title, int minutes, Visibility visibility, String instructorName) {
        super(title, minutes, visibility, ActivityCategory.LECTURE);
        this.instructorName = instructorName;
    }

    //getter
    public String getInstructorName() {
        return instructorName;
    }
}
