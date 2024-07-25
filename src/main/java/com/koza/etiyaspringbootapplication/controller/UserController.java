package com.koza.etiyaspringbootapplication.controller;
import com.koza.etiyaspringbootapplication.dto.request.CreateUserRequest;
import com.koza.etiyaspringbootapplication.dto.request.UpdateUserRequest;
import com.koza.etiyaspringbootapplication.dto.response.UserListResponse;
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

    @GetMapping("/getAllUser")
    public UserListResponse getAllUser(){
        return userService.getAllUser();
    }

    @PutMapping("/update/{userId}")
    public UserResponse updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request){
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/delete/{userId}")
    public UserResponse deleteUser(@PathVariable Long userId){
        return userService.deleteUser(userId);
    }
}
