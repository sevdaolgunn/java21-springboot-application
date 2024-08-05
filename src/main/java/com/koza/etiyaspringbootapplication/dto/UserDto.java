package com.koza.etiyaspringbootapplication.dto;

import com.koza.etiyaspringbootapplication.entity.User;
import lombok.Builder;
import lombok.Data;


@Builder
public record UserDto( Long id,
         String userName,
         String password,
         String email,
         boolean isSystemUser) {


}
