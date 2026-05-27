package com.sprintlog.sprintlogboot.service;


import com.sprintlog.sprintlogboot.domain.ActivityCategory;
import com.sprintlog.sprintlogboot.domain.LearningActivity;
import com.sprintlog.sprintlogboot.printer.ActivityPrinter;
import com.sprintlog.sprintlogboot.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityDashboard {

    private final ActivityRepository repository;

    @Autowired
    public ActivityDashboard(ActivityRepository repository) {
        if (repository == null)
            throw new IllegalArgumentException("학습 활동 목록 Cant b null");
        this.repository = repository;
    }

    /**
     * 카테고리별 활동 수를 세어 Summary를 만들자.
     *
     * */
    public Summary summarize() {

        // 로컬 클래스 선언
        class Counter{
            private int totalCount;
            private int lectureCount;
            private int practiceCount;
            private int readingCount;

            void add(LearningActivity activity) {
                totalCount++;
                // getCategory()는 LearningActivity의 public API
                switch (activity.getCategory()) {
                    case LECTURE  -> lectureCount++;
                    case PRACTICE -> practiceCount++;
                    case READING  -> readingCount++;
                }
            }

            Summary toSummary() {
                return new Summary(totalCount, lectureCount, practiceCount, readingCount);
            }
        }

        Counter counter = new Counter();
        for (LearningActivity activity : repository.findAll()) {
            counter.add(activity);
        }
        return counter.toSummary();

    }

    //내부 클래스
    public static class Summary {

        //필드
        private final int totalCount;
        private final int lectureCount;
        private final int practiceCount;
        private final int readingCount;

        //ctor
        public Summary(int totalCount, int lectureCount, int practiceCount, int readingCount) {
            this.totalCount = totalCount;
            this.lectureCount = lectureCount;
            this.practiceCount = practiceCount;
            this.readingCount = readingCount;
        }

        //getter
        public int getTotalCount() {
            return totalCount;
        }
        public int getLectureCount() {
            return lectureCount;
        }
        public int getPracticeCount() {
            return practiceCount;
        }
        public int getReadingCount() {
            return readingCount;
        }

    }

    /**
     * 보고서 출력기
     * 외부 클래스(ActivityDashboard)가 가지고 있는 activity 배열에 접근해야 하기 때문에
     * static을 붙이지 않은 멤버 내부 클래스로 선언.
     */
    public class ReportBuilder {

        //필드
        private final ActivityPrinter printer;

        //ctor
        public ReportBuilder(@Qualifier("console") ActivityPrinter printer) {
            if (printer == null) throw new IllegalArgumentException("printer cannot be null");

            this.printer = printer;
        }

        public void print(){
            Summary summary = summarize();  // 외부 클래스의 summarize() 호출
            System.out.println("── 활동 수: 총 " + summary.getTotalCount()
                    + "개 (강의 " + summary.getLectureCount()
                    + " / 실습 " + summary.getPracticeCount()
                    + " / 독서 " + summary.getReadingCount() + ")");

            for (LearningActivity activity : repository.findAll()) {  // 외부 클래스의 activities 접근
                printer.print(activity);
            }
        }
    }
    //카테고리별 그룹화 ------------------------
    // 카테고리별로 활동(Log)을 그룹화해서 Map으로 반환
    public Map<ActivityCategory, List<LearningActivity>> groupByCategory(){
        Map<ActivityCategory, List<LearningActivity>> map = new TreeMap<>();  //
        for (LearningActivity activity : repository.findAll()) {
            List<LearningActivity> list = new ArrayList<>();

            if (map.containsKey(activity.getCategory())){
                list = map.get(activity.getCategory());
            }

            list.add(activity);
            map.put(activity.getCategory(), list);
        }

        return map;
    }

    // 모든 활동에서 태그를 모아 알파벳 순 정렬 Set으로 반환한다.
    public Set<String> getSortedTagSet(){
        Set<String> tagSet = new TreeSet<>();
        for (LearningActivity activity : repository.findAll()) {
            tagSet.addAll(activity.getTags());
        }
        return Collections.unmodifiableSet(tagSet);
    }


    // 태그 필터링
    public List<LearningActivity> filterByTag(String tag){
        List<LearningActivity> result = new ArrayList<>();
        for (LearningActivity activity : repository.findAll()) {
            if (activity.hasTag(tag))
                result.add(activity);
        }
        return Collections.unmodifiableList(result);
    }



}
