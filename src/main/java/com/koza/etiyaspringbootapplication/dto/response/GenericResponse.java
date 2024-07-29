package com.koza.etiyaspringbootapplication.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class GenericResponse {
    private String message;
    private HttpStatus httpStatus;


}
