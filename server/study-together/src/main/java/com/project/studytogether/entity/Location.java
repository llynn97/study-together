package com.project.studytogether.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "location")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(columnDefinition = "TEXT")
    private String address;

    private Float latitude;
    private Float longitude;

    @OneToOne(mappedBy = "location")
    private Study study;

    public void setStudy(Study study) {
        study.setLocation(this);
    }
}
