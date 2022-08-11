package com.project.studytogether.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="interest_study")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interest_Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interest_study_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

}
