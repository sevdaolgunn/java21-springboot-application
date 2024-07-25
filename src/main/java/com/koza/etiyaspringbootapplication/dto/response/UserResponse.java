package com.koza.etiyaspringbootapplication.dto.response;

import com.koza.etiyaspringbootapplication.dto.UserDto;
import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class UserResponse {
    private UserDto user;
    private HttpStatus httpStatus;
}
