package com.koza.etiyaspringbootapplication.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {
    private Long id;
    private String shortCode;
}
