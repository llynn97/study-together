package com.project.studytogether.entity;


import com.project.studytogether.entity.enums.UserMethod;
import com.project.studytogether.entity.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(length = 100)
    private String email;

    @Column(length = 30, unique = true)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String password;

    @Column(columnDefinition = "TEXT")
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private UserMethod method;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "refreshToken")
    private String refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyUser> StudyUserList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterestStudy> InterestStudyList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notificationList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomJoin> chatRoomJoinList = new ArrayList<>();
}
