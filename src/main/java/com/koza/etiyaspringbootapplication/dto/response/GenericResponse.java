package com.koza.etiyaspringbootapplication.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder

public record GenericResponse( String message,
         HttpStatus httpStatus) {



}
