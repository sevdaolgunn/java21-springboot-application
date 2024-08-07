package com.koza.etiyaspringbootapplication.converter;

import com.koza.etiyaspringbootapplication.dto.UserDto;
import com.koza.etiyaspringbootapplication.dto.request.CreateUserRequest;
import com.koza.etiyaspringbootapplication.entity.User;
import com.koza.etiyaspringbootapplication.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User convertAsEntity(CreateUserRequest request){
        return User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(request.getPassword())
                .userStatus(request.getUserStatus())
                .birthDate(request.getBirthDate())
                .build();
    }
    public UserDto convertAsDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .email(user.getEmail())
                .userStatus(user.getUserStatus())
                .birthDate(user.getBirthDate())
                .isSystemUser(user.isSystemUser())
                .build();
    }
}
