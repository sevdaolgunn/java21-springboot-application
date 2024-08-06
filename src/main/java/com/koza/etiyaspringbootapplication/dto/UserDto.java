package com.koza.etiyaspringbootapplication.dto;


import com.koza.etiyaspringbootapplication.entity.UserStatus;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record UserDto( Long id,
         String userName,
         String password,
         String email,
         LocalDateTime birthDate,
         UserStatus userStatus,
         boolean isSystemUser) {


}
