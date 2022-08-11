package com.project.studytogether.entity;

import com.project.studytogether.entity.enums.StudyUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="study_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Study_User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long study_user_id;

    @Enumerated(EnumType.STRING)
    private StudyUserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_id")
    private Study study;

    @Column(columnDefinition = "TEXT")
    private String motive;





}
