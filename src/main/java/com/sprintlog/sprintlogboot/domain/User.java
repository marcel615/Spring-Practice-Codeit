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

}
