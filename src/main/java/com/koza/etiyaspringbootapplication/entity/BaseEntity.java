package com.koza.etiyaspringbootapplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;
@Getter
@Setter
@MappedSuperclass

public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime createDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;



    @PrePersist
    protected void onCreate(){
        LocalTime now = LocalTime.now();
        createDate = now;
        updateDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = new Date();
    }
}
