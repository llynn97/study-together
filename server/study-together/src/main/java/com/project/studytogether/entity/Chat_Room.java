package com.project.studytogether.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="chat_room")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat_Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chat_room_id;

    @DateTimeFormat
    private LocalDateTime create_date;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "study_id")
    private Study study;

    @OneToMany(mappedBy = "chat_room",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat_Room_Join> chatRoomJoinList=new ArrayList<>();

    @OneToMany(mappedBy = "chat_room",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messageList=new ArrayList<>();
}
