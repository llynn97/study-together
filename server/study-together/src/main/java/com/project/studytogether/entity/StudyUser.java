package com.project.studytogether.entity;

import com.project.studytogether.entity.enums.StudyUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "study_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_user_id")
    private Long studyUserId;

    @Enumerated(EnumType.STRING)
    private StudyUserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @Column(columnDefinition = "TEXT")
    private String motive;

}
