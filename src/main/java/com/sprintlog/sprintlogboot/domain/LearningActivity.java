package com.sprintlog.sprintlogboot.domain;


import com.sprintlog.sprintlogboot.excepion.InvalidActivityException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public abstract class LearningActivity implements Serializable {

    //이 파일의 클래스 구조가 현재 클래스와 같은지에 대한 버전 키 검사용 필드!
    private static final long serialVersionUID = 1L;

    //필드
    private static int totalObjectCreateCount = 0;

    private final long id;
    private String title;
    private int minutes;
    private Visibility visibility;
    private final ActivityCategory category;
    private final Set<String> tags = new HashSet<>();

    
    //ctor
    public LearningActivity(String title, int minutes, Visibility visibility, ActivityCategory category){
        validateTitle(title);
        validateMinutes(minutes);

        this.id = totalObjectCreateCount;
        this.title = title.trim();
        this.minutes = minutes;
        this.visibility = visibility;
        this.category = category;
        totalObjectCreateCount++;
    }

    //getter, setter
    public static int getTotalCreateCount(){
        return totalObjectCreateCount;
    }
    public long getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public int getMinutes(){
        return minutes;
    }
    public Visibility getVisibility(){
        return visibility;
    }
    public boolean isPublicActivity(){
        return visibility == Visibility.PUBLIC;
    }
    public ActivityCategory getCategory(){
        return category;
    }
    public Set<String> getTags(){
        return tags;
    }

    public void setTitle(String title){
        validateTitle(title);
        this.title = title;
    }
    public void setMinutes(int minutes){
        if (minutes <= 0) {
            System.out.println("잘못된 공부 시간입니다.");
            return;
        }
        this.minutes = minutes;
    }
    public void setVisibility(Visibility visibility){
        this.visibility = visibility;
    }
    public void addTag(String tag){
        if (tag == null || tag.isBlank())
            throw new InvalidActivityException("태그는 비워둘 수 없습니다.");

        tags.add(tag.trim().toLowerCase());
    }

    //method
    public void extendMinutes(int minutes){
        if (minutes <= 0) {
            throw new InvalidActivityException("추가 학습 시간은 1분 이상이어야 합니다. 입력값: " + minutes);
        }
        this.minutes += minutes;
    }
    public void changeTitle(String title){
        validateTitle(title);
        this.title = title;
    }
    public void openToPublic(){
        setVisibility(Visibility.PUBLIC);
    }
    public void hideToPublic(){
        setVisibility(Visibility.PRIVATE);
    }
    public String getVisibilityText(){
        return getVisibility() == Visibility.PUBLIC ? "공개" : "비공개";
    }
    public boolean hasTag(String tag){
        if (tag == null || tag.isBlank())
            return false;
        return tags.contains(tag.trim().toLowerCase());
    }

    private void validateTitle(String title){
        if (title == null || title.isBlank()) {
            throw new InvalidActivityException("학습 제목은 비워둘 수가 없습니다.");
        }
    }
    private void validateMinutes(int minutes){
        if (minutes <= 0) {
            throw new InvalidActivityException("학습 시간은 1분 이상이어야 합니다.");
        }
    }





}