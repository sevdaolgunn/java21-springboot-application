package com.koza.etiyaspringbootapplication.dto.response;

import com.koza.etiyaspringbootapplication.dto.UserDto;
import lombok.*;
import org.springframework.http.HttpStatus;


@Builder


public record UserResponse(UserDto user,
                           String message,
                           HttpStatus httpStatus) {

}
