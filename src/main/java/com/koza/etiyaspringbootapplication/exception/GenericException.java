package com.koza.etiyaspringbootapplication.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class GenericException extends RuntimeException {
    private HttpStatus httpStatus;
    private String errorMessage;

}
