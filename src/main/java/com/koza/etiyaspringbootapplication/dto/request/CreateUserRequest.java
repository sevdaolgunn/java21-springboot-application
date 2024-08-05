package com.koza.etiyaspringbootapplication.dto.request;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.koza.etiyaspringbootapplication.config.util.CustomLocalDateTimeDeserializer;
import com.koza.etiyaspringbootapplication.entity.UserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Setter
public class CreateUserRequest {
    private String userName;
    private String email;
    private String password;
    private UserStatus userStatus;

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime birthDate;







}
