package com.project.studytogether.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="location")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long location_id;

    @Column(columnDefinition = "TEXT")
    private String address;

    private Float latitude;
    private Float longitude;

    @OneToOne(mappedBy = "location")
    private Study study;
}
