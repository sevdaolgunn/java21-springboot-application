package com.koza.etiyaspringbootapplication.controller;

import com.koza.etiyaspringbootapplication.dto.RoleDto;
import com.koza.etiyaspringbootapplication.dto.request.RoleRequest;
import com.koza.etiyaspringbootapplication.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleRequest request){
        RoleDto roleDto = roleService.createRole(request);
        return ResponseEntity.ok(roleDto);
    }

    @GetMapping("{roleId}")
    public ResponseEntity<RoleDto> getRole(@PathVariable Long roleId){
        RoleDto roleDto = roleService.getRole(roleId);
        return ResponseEntity.ok(roleDto);

    }
    @PutMapping("{roleId}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long roleId, @RequestBody RoleRequest request){
        RoleDto roleDto =  roleService.updateRole(roleId, request);
        return ResponseEntity.ok(roleDto);
    }

    @DeleteMapping("{roleId}")
    public ResponseEntity<RoleDto> delete(@PathVariable Long roleId){
        RoleDto roleDto = roleService.deleteRole(roleId);
        return ResponseEntity.ok(roleDto);
    }
}
