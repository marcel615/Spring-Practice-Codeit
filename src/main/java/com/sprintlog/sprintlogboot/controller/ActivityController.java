package com.sprintlog.sprintlogboot.controller;

import com.sprintlog.sprintlogboot.domain.*;
import com.sprintlog.sprintlogboot.dto.request.CreateActivityRequest;
import com.sprintlog.sprintlogboot.dto.request.UpdateActivityRequest;
import com.sprintlog.sprintlogboot.excepion.ActivityNotFoundException;
import com.sprintlog.sprintlogboot.repository.ActivityRepository;
import com.sprintlog.sprintlogboot.service.ActivityDashboard;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Comparator;
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
    public String hello() {
        log.info("hello");
        return "hello";
    }

    // 모든 활동 목록
    @GetMapping()
    public ResponseEntity<List<LearningActivity>> getAll(
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword) {

        Comparator<LearningActivity> comparator = switch (sort) {
            case "minutes" -> Comparator.comparing(LearningActivity::getMinutes);
            case "title" -> Comparator.comparing(LearningActivity::getTitle);
            default -> Comparator.comparing(LearningActivity::getId);
        };

        List<LearningActivity> list = repository.findAll().stream()
                .sorted(comparator)
                .skip((long) page * size)
                .limit(size)
                .toList();

        return ResponseEntity.ok().body(list);
    }

    // 특정 활동 세부 사항
    @GetMapping("/{id}")
    public ResponseEntity<LearningActivity> getById(@PathVariable Long id) {
        LearningActivity first = repository.findFirst(activity -> activity.getId() == id)
                .orElseThrow(() -> new ActivityNotFoundException(id));

        return ResponseEntity.ok().body(first);
    }

    //카테고리별로 그룹화된 활동 목록
    @GetMapping("/dashboard")
    public ResponseEntity<Map<ActivityCategory, List<LearningActivity>>> getAllByCategory() {
        return ResponseEntity.ok().body(activityDashboard.groupByCategory());
    }

    //활동 수 요약 정보
    @GetMapping("/summary")
    public ResponseEntity<ActivityDashboard.Summary> getSummary() {
        return ResponseEntity.ok().body(activityDashboard.summarize());
    }

    //태그로 활동을 필터링
    @GetMapping("/search")
    public ResponseEntity<List<LearningActivity>> searchByTag(@RequestParam String tag,
                                                              @RequestParam String name,
                                                              @RequestParam int age) {

        return ResponseEntity.ok().body(activityDashboard.filterByTag(tag));
    }

    // 변경 작업: -- 생성(Post) / 수정(Put) / 삭제(Delete) --
    @PostMapping
    public ResponseEntity<LearningActivity> create(@Valid @RequestBody CreateActivityRequest request) {
        LearningActivity activity = toActivity(request);
        repository.add(activity);

        // 성공 시 201 Created + Location 헤더(생성된 자원의 주소)를 함께 응답한다.
        URI location = URI.create("/api/activities/" + activity.getId());
        return ResponseEntity.created(location).body(activity);
    }

    //활동 수정, 자원 식별은 Path(/{id}), 변경할 내용은 본문(UpdateActivityRequest)
    //대상이 없으면 404, 있으면 제목, 공개여부를 변경하고 200.
    @PatchMapping("/{id}")
    public ResponseEntity<LearningActivity> update(@PathVariable Long id, @Valid @RequestBody UpdateActivityRequest request) {
        LearningActivity first = repository.findFirst(activity -> activity.getId() == id)
                .orElseThrow(() -> new ActivityNotFoundException(id));

        return ResponseEntity.ok().body(first);
    }


    //활동 삭제, 성공 시 본문 없이 204 No Content, 대상이 없으면 404.
    @DeleteMapping("/{id}")
    public ResponseEntity<LearningActivity> delete(@PathVariable Long id) {
        LearningActivity first = repository.findFirst(activity -> activity.getId() == id)
                .orElseThrow(() -> new ActivityNotFoundException(id));

        repository.delete(first);
        return ResponseEntity.noContent().build();
    }

    private LearningActivity toActivity(CreateActivityRequest request) {
        LearningActivity activity = switch (request.type()) {
            case LECTURE ->
                    new LectureLog(request.title(), request.minutes(), request.visibility(), request.instructorName());
            case READING ->
                    new ReadingLog(request.title(), request.minutes(), request.visibility(), request.bookTitle());
            case PRACTICE ->
                    new PracticeLog(request.title(), request.minutes(), request.visibility(), request.completionRate());
        };

        if (request.tags() != null) {
            request.tags().forEach(activity::addTag);
        }

        return activity;
    }
}
