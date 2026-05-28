package com.sprintlog.sprintlogboot.controller;

import com.sprintlog.sprintlogboot.domain.ActivityCategory;
import com.sprintlog.sprintlogboot.domain.LearningActivity;
import com.sprintlog.sprintlogboot.repository.ActivityRepository;
import com.sprintlog.sprintlogboot.service.ActivityDashboard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityRepository repository;
    private final ActivityDashboard activityDashboard;

    @GetMapping("/hello")
    public String hello(){
        log.info("hello");
        return "hello";
    }

    // 모든 활동 목록
    @GetMapping()
    public ResponseEntity<List<LearningActivity>> getAll(){
        return ResponseEntity.ok().body(repository.findAll());
    }

    // 특정 활동 세부 사항
    @GetMapping("/{id}")
    public ResponseEntity<LearningActivity> getById(@PathVariable Long id){
        Optional<LearningActivity> first = repository.findFirst(activity -> activity.getId() == id);
        if (first.isPresent()){
            return ResponseEntity.ok().body(first.get());
        }
        return ResponseEntity.notFound().build();
    }

    //카테고리별로 그룹화된 활동 목록
    @GetMapping("/dashboard")
    public ResponseEntity<Map<ActivityCategory, List<LearningActivity>>> getAllByCategory(){
        return ResponseEntity.ok().body(activityDashboard.groupByCategory());
    }

    //활동 수 요약 정보
    @GetMapping("/summary")
    public ResponseEntity<ActivityDashboard.Summary> getSummary(){
        return ResponseEntity.ok().body(activityDashboard.summarize());
    }

    //태그로 활동을 필터링
    @GetMapping("/search")
    public ResponseEntity<List<LearningActivity>> searchByTag(@RequestParam String tag, @RequestParam String name, @RequestParam int age){
        return ResponseEntity.ok().body(activityDashboard.filterByTag(tag));
    }
}
