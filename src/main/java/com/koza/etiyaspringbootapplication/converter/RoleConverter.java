package com.koza.etiyaspringbootapplication.converter;

import com.koza.etiyaspringbootapplication.dto.RoleDto;
import com.koza.etiyaspringbootapplication.dto.request.RoleRequest;
import com.koza.etiyaspringbootapplication.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter {

    public Role convertAsEntity(RoleRequest request){
        return Role.builder()
                .roleName(request.getRoleName())
                .description(request.getDescription())
                .shortCode(request.getShortCode())
                .build();
    }

    public RoleDto convertAsDto(Role role){
        return RoleDto.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .shortCode(role.getShortCode())
                .build();
    }
}
