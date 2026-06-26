package com.sprintlog.sprintlogboot.domain;

import jakarta.persistence.*;
import org.springframework.data.domain.Auditable;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    //JPA가 엔터티를 만들 때 사용하는 기본 생성자. 우리가 호출하는 게 아님
    protected User() {

    }

    //우리가 실제로 사용하는 생성자
    public User(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
