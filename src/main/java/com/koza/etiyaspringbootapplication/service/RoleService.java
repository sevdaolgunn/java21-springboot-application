package com.koza.etiyaspringbootapplication.service;

import com.koza.etiyaspringbootapplication.converter.RoleConverter;
import com.koza.etiyaspringbootapplication.dto.RoleDto;
import com.koza.etiyaspringbootapplication.dto.request.RoleRequest;
import com.koza.etiyaspringbootapplication.dto.response.GenericResponse;
import com.koza.etiyaspringbootapplication.dto.response.RoleResponse;
import com.koza.etiyaspringbootapplication.entity.Role;
import com.koza.etiyaspringbootapplication.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;

    public RoleResponse createRole(RoleRequest request){
        Role role = roleRepository.save(roleConverter.convertAsEntity(request));
        return RoleResponse.builder()
                .role(roleConverter.convertAsDto(role))
                .build();
    }

    protected Role findById(Long roleId){
        return roleRepository.findById(roleId).orElseThrow(
                ()->new RuntimeException("Böyle bir role bulunamadı")
        );
    }

    public RoleResponse getRole(Long roleId){
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isEmpty()){
            return RoleResponse.builder()
                    .message("Not found")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }

        Role role = optionalRole.get();
        RoleDto roleDto = roleConverter.convertAsDto(role);
        return RoleResponse.builder()
                .role(roleDto)
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .build();
    }

    public RoleResponse updateRole(Long roleId, RoleRequest request){
        Role role = findById(roleId);
        role.setShortCode(request.getShortCode());

        roleRepository.save(role);
        return RoleResponse.builder()
                .role(roleConverter.convertAsDto(role))
                .message("Updated!")
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public GenericResponse deleteRole(Long roleId){
        Role role = findById(roleId);
        roleRepository.delete(role);
        return GenericResponse.builder()
                .message("DELETED!")
                .httpStatus(HttpStatus.ACCEPTED)
                .build();
    }
}
