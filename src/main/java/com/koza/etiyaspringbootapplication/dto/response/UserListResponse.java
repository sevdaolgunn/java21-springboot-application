package com.koza.etiyaspringbootapplication.dto.response;

import com.koza.etiyaspringbootapplication.dto.UserDto;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder

public record UserListResponse(List<UserDto> userList,
                               HttpStatus httpStatus) {

}
