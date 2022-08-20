package com.project.studytogether.entity;

import com.project.studytogether.entity.enums.StudyMethod;
import com.project.studytogether.entity.enums.StudyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "study")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long studyId;

    @Column(columnDefinition = "integer default 1")
    private Integer headcount;

    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;

    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private StudyMethod method;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime writtenDate;

    @Column(length = 100)
    private String title;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterestStudy> interestStudyList = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyUser> studyUserList = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categoryList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToOne(mappedBy = "study")
    private ChatRoom chatRoom;

}
