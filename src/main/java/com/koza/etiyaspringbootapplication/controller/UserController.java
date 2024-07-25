package com.koza.etiyaspringbootapplication.controller;

import com.koza.etiyaspringbootapplication.dto.UserDto;
import com.koza.etiyaspringbootapplication.dto.request.CreateUserRequest;
import com.koza.etiyaspringbootapplication.dto.response.UserResponse;
import com.koza.etiyaspringbootapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
