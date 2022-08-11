package com.project.studytogether.entity;


import com.project.studytogether.entity.enums.UserMethod;
import com.project.studytogether.entity.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name="user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(length = 100)
    private String email;

    @Column(length = 30,unique = true)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String password;

    @Column(columnDefinition = "TEXT")
    private String profile_url;

    @Enumerated(EnumType.STRING)
    private UserMethod method;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Study_User> StudyUserList=new ArrayList<>();

    @OneToMany(mappedBy ="user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interest_Study> InterestStudyList=new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notificationList=new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messageList=new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat_Room_Join> chatRoomJoinList=new ArrayList<>();
}
