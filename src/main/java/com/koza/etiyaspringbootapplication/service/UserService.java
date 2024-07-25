package com.koza.etiyaspringbootapplication.service;

import com.koza.etiyaspringbootapplication.converter.UserConverter;
import com.koza.etiyaspringbootapplication.dto.UserDto;
import com.koza.etiyaspringbootapplication.dto.request.CreateUserRequest;
import com.koza.etiyaspringbootapplication.dto.response.UserResponse;
import com.koza.etiyaspringbootapplication.entity.User;
import com.koza.etiyaspringbootapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

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


}
