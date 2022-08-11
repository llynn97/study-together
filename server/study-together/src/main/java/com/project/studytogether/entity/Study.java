package com.project.studytogether.entity;

import com.project.studytogether.entity.enums.StudyMethod;
import com.project.studytogether.entity.enums.StudyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name="study")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long study_id;

    @Column(columnDefinition = "integer default 1")
    private Integer headcount;

    @Enumerated(EnumType.STRING)
    private StudyStatus study_status;

    @Temporal(TemporalType.DATE)
    private Date start_date;

    @Enumerated(EnumType.STRING)
    private StudyMethod method;

    @Column(columnDefinition = "TEXT")
    private String content;

    @DateTimeFormat
    private LocalDateTime written_date;

    @Column(length = 100)
    private String title;

    @OneToMany(mappedBy = "study",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interest_Study> interestStudyList=new ArrayList<>();

    @OneToMany(mappedBy = "study",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Study_User> studyUserList=new ArrayList<>();

    @OneToMany(mappedBy = "study",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categoryList=new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="location_id")
    private Location location;

    @OneToOne(mappedBy = "study")
    private Chat_Room chat_room;
}
