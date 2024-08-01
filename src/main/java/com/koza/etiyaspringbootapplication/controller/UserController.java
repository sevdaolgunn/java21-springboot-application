package com.koza.etiyaspringbootapplication.controller;

import com.koza.etiyaspringbootapplication.dto.request.CreateUserRequest;
import com.koza.etiyaspringbootapplication.dto.request.UpdateUserRequest;
import com.koza.etiyaspringbootapplication.dto.response.GenericResponse;
import com.koza.etiyaspringbootapplication.dto.response.UserListResponse;
import com.koza.etiyaspringbootapplication.dto.response.UserResponse;
import com.koza.etiyaspringbootapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/createUser")
    public UserResponse createUser(@RequestBody CreateUserRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/getUser/{userId}")
    public UserResponse getUser(@PathVariable Long userId){
        return userService.getUser(userId);
    }

    @GetMapping("/getAllUser")
    public UserListResponse getAllUser(){
        return userService.getAllUser();
    }

    @PutMapping("/update/{userId}")
    public GenericResponse updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request){
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/delete/{userId}")
    public GenericResponse deleteUser(@PathVariable Long userId){
        return userService.deleteUser(userId);
    }

    @PostMapping("/{userId}/users/{roleId}")
    public UserResponse enrollUserInRole(@PathVariable Long userId, @PathVariable Long roleId){
        return userService.enrollUserInRole(userId, roleId);
    }
    @PostMapping("/{userId}/roles")
    public GenericResponse addRolesToUser(@PathVariable Long userId, @RequestBody List<String> roleNames){
        return userService.addRolesToUser(userId, roleNames);
    }
    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<String>> getUserRoles(@PathVariable Long userId){
        return userService.getUserRoles(userId);
    }
}

