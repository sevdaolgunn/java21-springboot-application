package com.koza.etiyaspringbootapplication.dto;

import lombok.Builder;


@Builder
public record RoleDto( Long id,
         String roleName,
         String description,
         String shortCode) {

}
