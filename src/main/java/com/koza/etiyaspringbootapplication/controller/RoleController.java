package com.koza.etiyaspringbootapplication.controller;

import com.koza.etiyaspringbootapplication.dto.request.RoleRequest;
import com.koza.etiyaspringbootapplication.dto.response.GenericResponse;
import com.koza.etiyaspringbootapplication.dto.response.RoleResponse;
import com.koza.etiyaspringbootapplication.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/createRole")
    public RoleResponse createRole(@RequestBody RoleRequest request){
        return roleService.createRole(request);
    }

    @GetMapping("/getRole/{roleId}")
    public RoleResponse getRole(@PathVariable Long roleId){
        return roleService.getRole(roleId);

    }
    @PutMapping("/update/{roleId}")
    public RoleResponse updateRole(@PathVariable Long roleId, @RequestBody RoleRequest request){
        return roleService.updateRole(roleId, request);
    }

    @DeleteMapping("/delete/{roleId}")
    public GenericResponse delete(@PathVariable Long roleId){
        return roleService.deleteRole(roleId);
    }
}
