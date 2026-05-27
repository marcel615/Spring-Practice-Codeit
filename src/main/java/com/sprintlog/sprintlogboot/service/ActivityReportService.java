package com.sprintlog.sprintlogboot.service;

import com.sprintlog.sprintlogboot.domain.LearningActivity;
import com.sprintlog.sprintlogboot.policy.Reviewable;
import com.sprintlog.sprintlogboot.printer.ActivityPrinter;
import com.sprintlog.sprintlogboot.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ActivityReportService {

    private final ActivityPrinter printer;
    private final ActivityRepository repository;

    public ActivityReportService(@Qualifier("console") ActivityPrinter printer, ActivityRepository repository) {
        if (printer == null) {
            throw new IllegalArgumentException("출력 도구는 반드시 필요합니다.");
        }
        if (repository == null) {
            throw new IllegalArgumentException("Repository는 null일 수 없습니다.");
        }
        this.printer = printer;
        this.repository = repository;
    }

    public void printAll(){
        for (LearningActivity activity : repository.findAll()) {
            printer.print(activity);
        }
    }

    public void printNeedsReview(){
        for (LearningActivity activity : repository.findAll()) {
            if (activity instanceof Reviewable r && r.needsReview()) {
                r.printReviewTarget();
            }
        }
    }


}
