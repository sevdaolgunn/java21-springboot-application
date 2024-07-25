package com.koza.etiyaspringbootapplication.dto.request;

import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;



@RequiredArgsConstructor
@Getter
@Setter
public class UpdateUserRequest {
    private String userName;
    private String email;
    private String password;



}
