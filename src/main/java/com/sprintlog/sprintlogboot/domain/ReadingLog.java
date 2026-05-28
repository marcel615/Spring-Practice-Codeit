package com.sprintlog.sprintlogboot.domain;


import java.io.Serializable;

public class ReadingLog extends LearningActivity implements Serializable {

    //이 파일의 클래스 구조가 현재 클래스와 같은지에 대한 버전 키 검사용 필드!
    private static final long serialVersionUID = 1L;

    //필드
    private String bookTitle;

    //ctor
    public ReadingLog(String title, int minutes, Visibility visibility, String bookTitle) {
        super(title, minutes, visibility, ActivityCategory.READING);
        this.bookTitle = bookTitle;
    }

    //getter
    public String getBookTitle() {
        return bookTitle;
    }
}
