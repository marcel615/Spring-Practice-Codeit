package com.sprintlog.sprintlogboot.dto.request;

import com.sprintlog.sprintlogboot.domain.ActivityCategory;
import com.sprintlog.sprintlogboot.domain.Visibility;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CreateActivityRequest(

        @NotNull(message = "활동 유형(type)은 필수입니다.")
        ActivityCategory type,

        @NotBlank(message = "제목은 비워둘 수 없습니다.")
        String title,

        @Min(value = 1, message = "학습 시간은 1분 이상이어야 합니다.")
        @Max(value = 1440, message = "학습 시간은 하루(1440분)를 넘을 수 없습니다.")
        int minutes,

        @NotNull(message = "공개 여부는 필수입니다.")
        Visibility visibility,

        Set<String> tags,
        String instructorName,
        int completionRate,
        String bookTitle
) {
}
