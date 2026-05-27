package com.sprintlog.sprintlogboot.policy;

public interface Reviewable {

    boolean needsReview();
    void printReviewTarget();
}
