package com.koza.etiyaspringbootapplication.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CreateUserRequest {
    private String userName;
    private String email;
    private String password;

}
