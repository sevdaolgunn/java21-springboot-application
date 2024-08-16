package com.koza.etiyaspringbootapplication.service;

import com.koza.etiyaspringbootapplication.converter.UserConverter;
import com.koza.etiyaspringbootapplication.dto.UserDto;
import com.koza.etiyaspringbootapplication.dto.request.CreateUserRequest;
import com.koza.etiyaspringbootapplication.dto.request.UpdateUserRequest;
import com.koza.etiyaspringbootapplication.entity.Role;
import com.koza.etiyaspringbootapplication.entity.User;
import com.koza.etiyaspringbootapplication.entity.UserStatus;
import com.koza.etiyaspringbootapplication.exception.ModelNotFoundException;
import com.koza.etiyaspringbootapplication.repository.RoleRepository;
import com.koza.etiyaspringbootapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RoleRepository roleRepository;


    public UserDto createUser(CreateUserRequest request){
        User user = userConverter.convertAsEntity(request);
        user.setUserStatus(UserStatus.CREATED);
        user = userRepository.save(user);

        return userConverter.convertAsDto(user);
    }
    protected User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(
                ()-> new ModelNotFoundException("Böyle bir kullanıcı bulunamamıştır!")
        );
    }

    public UserDto getUser(Long userId){
        Optional<User> optionalUser = Optional.ofNullable(findById(userId));
        User user = optionalUser.get();

        return userConverter.convertAsDto(user);
    }

    public List<UserDto> getAllUser(){
        List<User> userList = userRepository.findAll();
        if(userList.isEmpty()){
            throw new ModelNotFoundException("Kayıtlı kullanıcı bulunamadı!");
        }
        List<UserDto> userDtoList = new ArrayList<>();
        for (int i=0; i < userList.size(); i++) {
            User user =userList.get(i);
            userDtoList.add(userConverter.convertAsDto(user));
        }
        return userDtoList;
    }

    public UserDto updateUser( Long userId, UpdateUserRequest request){
        Optional<User> optionalUser = Optional.ofNullable(findById(userId));

        User user = optionalUser.get();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        user =  userRepository.save(user);

        return userConverter.convertAsDto(user);
    }

    public UserDto deleteUser(Long userId){
        User user = findById(userId);
        userRepository.delete(user);
        return userConverter.convertAsDto(user);
    }


    public UserDto addRolesToUser(Long userId, List<String> roleNames){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ModelNotFoundException("Böyle bir kullanıcı bulunamadı!");
        }

        User user = optionalUser.get();

        for (String roleName : roleNames){
            Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);
            if (optionalRole.isEmpty()){
                throw new ModelNotFoundException("Böyle bir role bulunamadı!");
            }
            Role role = optionalRole.get();
            user.getRoles().add(role);

        }
        user.setSystemUser(true);
        user = userRepository.save(user);
        return userConverter.convertAsDto(user);
    }

    public ResponseEntity<List<String>> getUserRoles(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        List<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

}
