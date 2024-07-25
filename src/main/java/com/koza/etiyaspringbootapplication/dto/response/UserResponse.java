package com.koza.etiyaspringbootapplication.dto.response;

import com.koza.etiyaspringbootapplication.dto.UserDto;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserResponse {
    private UserDto user;
    private String message;
    private HttpStatus httpStatus;
}
