package com.koza.etiyaspringbootapplication.dto.response;

import com.koza.etiyaspringbootapplication.dto.UserDto;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserListResponse {
    private List<UserDto> userList;
    private HttpStatus httpStatus;
}
