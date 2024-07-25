package com.koza.etiyaspringbootapplication.dto;

import com.koza.etiyaspringbootapplication.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    private String email;
    private boolean isSystemUser;

}
