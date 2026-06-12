package com.sprintlog.sprintlogboot.controller;

import com.sprintlog.sprintlogboot.domain.*;
import com.sprintlog.sprintlogboot.dto.request.CreateActivityRequest;
import com.sprintlog.sprintlogboot.dto.request.UpdateActivityRequest;
import com.sprintlog.sprintlogboot.excepion.ActivityNotFoundException;
import com.sprintlog.sprintlogboot.repository.ActivityRepository;
import com.sprintlog.sprintlogboot.service.ActivityDashboard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping({"/api/v1/activities", "/api/activities"}) // 경로를 둘로 받아서 기존의 요청도 해결할 수 있도록.
@Tag(name = "활동(Activity)", description = "학습 활동 조회, 생성, 수정, 삭제 API")
public class ActivityController {

    private final ActivityRepository repository;
    private final ActivityDashboard activityDashboard;

    @GetMapping("/hello")
    public String hello() {
        log.info("hello");
        return "hello";
    }

    // 모든 활동 목록
    @Operation(summary = "활동 목록 조회", description = "정렬(sort), 페이지(page), 크기(size) 쿼리파라미터로 활동 목록을 가볍게 요약해 반환한다.")
    @ApiResponse(responseCode = "200", description = "조회 성공(요약 목록)")
    @GetMapping()
    public ResponseEntity<List<LearningActivity>> getAll(
            @Parameter(description = "정렬 기준", example = "id", schema = @Schema(allowableValues = {"id", "minutes", "title"}))
            @RequestParam(defaultValue = "id") String sort,

            @Parameter(description = "페이지 번호(0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "한 화면에 보여질 데이터 크기", example = "20")
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
    @Operation(summary = "활동 단건 조회", description = "id로 활동 하나를 상세하게 반환한다. 없으면 404(ProblemDetail)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 id의 활동이 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActivityNotFoundException.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "about:blank",
                                                "title": "활동을 찾을 수 없음",
                                                "status": 404,
                                                "detail": "활동을 찾을 수 없습니다. id=222",
                                                "instance": "/api/activities/222",
                                                "timestamp": "2026-06-12T01:38:25.989279Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<LearningActivity> getById(
            @Parameter(description = "활동 식별자", example = "1")
            @PathVariable Long id) {
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

        return ResponseEntity.ok()
                .header("Deprecation", "true")
                .header("Sunset", "Thu, 31 Dec 2026 23:59:59 GMT")
                .header("Link",
                        "<https://docs.sprintlog.example/guides/migration#search>; rel=\"deprecation\"")
                .body(activityDashboard.filterByTag(tag));
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
    @PutMapping("/{id}")
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
