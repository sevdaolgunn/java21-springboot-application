package com.koza.etiyaspringbootapplication.controller;

import com.koza.etiyaspringbootapplication.dto.UserDto;
import com.koza.etiyaspringbootapplication.dto.request.CreateUserRequest;
import com.koza.etiyaspringbootapplication.dto.request.UpdateUserRequest;
import com.koza.etiyaspringbootapplication.service.CSVService;
import com.koza.etiyaspringbootapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final CSVService csvService;



    @PostMapping("/import-csv")
    public ResponseEntity<String> importUsersFromCSV(@RequestParam("file") MultipartFile file) {
        try {
            if (!isValidCSVFormat(file)) {
                return ResponseEntity.badRequest().body("Geçersiz CSV formatı.");
            }
            csvService.saveUsersFromCSV(file);
            return ResponseEntity.ok("Kullanıcılar başarıyla içe aktarıldı.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("İçe aktarma sırasında bir hata oluştu: " + e.getMessage());
        }
    }
    private boolean isValidCSVFormat(MultipartFile file) {
        String contentType = file.getContentType();
        return "text/csv".equals(contentType) || "application/vnd.ms-excel".equals(contentType);
    }

    @GetMapping("/export-csv")
    public ResponseEntity<ByteArrayResource> exportToCSV() throws Exception {
        ByteArrayResource resource = csvService.exportUsersToCSV("User");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=users.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }


    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest request){
        UserDto userDto =  userService.createUser(request);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId){
       UserDto userDto =  userService.getUser(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUser(){
        List<UserDto> userDtoList = userService.getAllUser();
        return ResponseEntity.ok(userDtoList);
    }

    @PutMapping("{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request){
        UserDto userDto = userService.updateUser(userId, request);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long userId){
        UserDto userDto = userService.deleteUser(userId);
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserDto> addRolesToUser(@PathVariable Long userId, @RequestBody List<String> roleNames){
        UserDto userDto = userService.addRolesToUser(userId, roleNames);
        return ResponseEntity.ok(userDto);
    }
    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<String>> getUserRoles(@PathVariable Long userId){
        return userService.getUserRoles(userId);
    }



}

