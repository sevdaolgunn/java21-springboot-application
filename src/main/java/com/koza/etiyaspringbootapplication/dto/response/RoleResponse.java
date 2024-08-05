package com.koza.etiyaspringbootapplication.dto.response;

import com.koza.etiyaspringbootapplication.dto.RoleDto;
import lombok.*;
import org.springframework.http.HttpStatus;

@Builder

public record RoleResponse(RoleDto role,
                           String message,
                           HttpStatus httpStatus) {

}
