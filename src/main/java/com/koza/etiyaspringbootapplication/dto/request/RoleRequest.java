package com.koza.etiyaspringbootapplication.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class RoleRequest {
    private String shortCode;
    private String roleName;
    private String description;

}
