package com.koza.etiyaspringbootapplication.service;

import com.koza.etiyaspringbootapplication.converter.RoleConverter;
import com.koza.etiyaspringbootapplication.dto.RoleDto;
import com.koza.etiyaspringbootapplication.dto.request.RoleRequest;
import com.koza.etiyaspringbootapplication.entity.Role;
import com.koza.etiyaspringbootapplication.exception.ModelNotFoundException;
import com.koza.etiyaspringbootapplication.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;

    public RoleDto createRole(RoleRequest request){
        Role role = roleRepository.save(roleConverter.convertAsEntity(request));
        return roleConverter.convertAsDto(role);
    }

    protected Role findById(Long roleId){
        return roleRepository.findById(roleId).orElseThrow(
                ()-> new ModelNotFoundException("Böyle bir rol bulunamamıştır!")
        );
    }

    public RoleDto getRole(Long roleId){
        Optional<Role> optionalRole = Optional.ofNullable(findById(roleId));

        Role role = optionalRole.get();
        return roleConverter.convertAsDto(role);

    }

    public RoleDto updateRole(Long roleId, RoleRequest request){
        Role role = findById(roleId);
        role.setShortCode(request.getShortCode());
        role.setRoleName(request.getRoleName());
        role.setDescription(request.getDescription());

        roleRepository.save(role);
        return roleConverter.convertAsDto(role);
    }


    public RoleDto deleteRole(Long roleId){
        Role role = findById(roleId);
        roleRepository.delete(role);
        return roleConverter.convertAsDto(role);
    }
}
