package com.sprintlog.sprintlogboot.dto.request;

import com.sprintlog.sprintlogboot.domain.Visibility;

public record UpdateActivityRequest(
        String title,
        Visibility visibility
) {
}
