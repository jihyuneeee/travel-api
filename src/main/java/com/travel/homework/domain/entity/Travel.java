package com.travel.homework.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@javax.persistence.Entity
@Getter
@Setter
@Table(name = "travels")
@DynamicUpdate
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long id;

    private String travelname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    @JsonIgnore
    private City city;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",
    // timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate; // 여행시작 시간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate; // 여행 종료 시간

    // ==생성 메서드==//
    public static Travel planTravel(String travelname, City city, Users user, LocalDateTime startDate,
            LocalDateTime endDate) {

        Travel travel = new Travel();
        travel.setTravelname(travelname);
        travel.setCity(city);
        travel.setUser(user);
        travel.setStartDate(startDate);
        travel.setEndDate(endDate);

        return travel;
    }
}
