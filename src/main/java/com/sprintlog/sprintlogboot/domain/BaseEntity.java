package com.sprintlog.sprintlogboot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 이 클래스 자체는 테이블이 되지 않는다. 대신 이 클래스를 상속한 엔티티의 테이블에 여기 선언된 컬럼이 합쳐져서 들어간다.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //생성 시각 - 처음 저장될 때 한 번 채워지고, 이후 바뀌지 않는다.
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 수정 시각 - 저장될 때마다 현재 시각으로 갱신
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
