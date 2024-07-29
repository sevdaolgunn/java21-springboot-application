package com.koza.etiyaspringbootapplication.service;

import com.koza.etiyaspringbootapplication.converter.UserConverter;
import com.koza.etiyaspringbootapplication.dto.UserDto;
import com.koza.etiyaspringbootapplication.dto.request.CreateUserRequest;
import com.koza.etiyaspringbootapplication.dto.request.UpdateUserRequest;
import com.koza.etiyaspringbootapplication.dto.response.GenericResponse;
import com.koza.etiyaspringbootapplication.dto.response.UserListResponse;
import com.koza.etiyaspringbootapplication.dto.response.UserResponse;
import com.koza.etiyaspringbootapplication.entity.Role;
import com.koza.etiyaspringbootapplication.entity.User;
import com.koza.etiyaspringbootapplication.repository.RoleRepository;
import com.koza.etiyaspringbootapplication.repository.UserRepository;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RoleRepository roleRepository;


    public UserResponse createUser(CreateUserRequest request){
        User user = userRepository.save(userConverter.convertAsEntity(request));
        return UserResponse.builder()
                .user(userConverter.convertAsDto(user))
                .build();
    }
    protected User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(
                ()-> new RuntimeException("Böyle bir kullanıcı bulunamadı.")
        );
    }

    public UserResponse getUser(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            return UserResponse.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
        User user = optionalUser.get();
        UserDto userDto = userConverter.convertAsDto(user);
        return UserResponse.builder()
                .user(userDto)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public UserListResponse getAllUser(){
        List<User> userList = userRepository.findAll();
        if(userList.isEmpty()){
            return UserListResponse.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .build();
        }
        List<UserDto> userDtoList = new ArrayList<>();
        for (int i=0; i < userList.size(); i++) {
            User user =userList.get(i);
            userDtoList.add(userConverter.convertAsDto(user));
        }
        return UserListResponse.builder()
                .userList(userDtoList)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public UserResponse updateUser( Long userId, UpdateUserRequest request){
        User user =findById(userId);
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());


        userRepository.save(user);

        return UserResponse.builder()
                .user(userConverter.convertAsDto(user))
                .httpStatus(HttpStatus.ACCEPTED)
                .build();
    }

    public GenericResponse deleteUser(Long userId){
        User user = findById(userId);
        userRepository.delete(user);
        return GenericResponse.builder()
                .message("DELETED!")
                .httpStatus(HttpStatus.ACCEPTED)
                .build();
    }

    public UserResponse enrollUserInRole(Long userId, Long roleId){
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Role> roleOptional = roleRepository.findById(roleId);

        if (userOptional.isPresent() && roleOptional.isPresent()){
            User user = userOptional.get();
            Role role = roleOptional.get();

            user.getRoles().add(role);
            user.setSystemUser(true);
            user.setShortCode(role.getShortCode());
            role.getUsers().add(user);
            userRepository.save(user);
        }
        return UserResponse.builder()
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public UserListResponse getUsersByRole(String shortCode){
        List<User> userList = userRepository.findByShortCode(shortCode);
        List<UserDto> userDtoList = new ArrayList<>();
        for(int i = 0; i<userList.size(); i++) {
            User user = userList.get(i);
            userDtoList.add(userConverter.convertAsDto(user));
        }

        return UserListResponse.builder()
                    .userList(userDtoList)
                    .build();
    }
}
