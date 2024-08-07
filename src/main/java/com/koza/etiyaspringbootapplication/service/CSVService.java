package com.koza.etiyaspringbootapplication.service;


import com.koza.etiyaspringbootapplication.entity.Role;
import com.koza.etiyaspringbootapplication.entity.User;
import com.koza.etiyaspringbootapplication.repository.RoleRepository;
import com.koza.etiyaspringbootapplication.repository.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CSVService {

    @Autowired
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public void saveUsersFromCSV(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            List<User> users = new ArrayList<>();

            for (CSVRecord csvRecord : csvParser) {
                String id = csvRecord.get("id");
                String userName = csvRecord.get("userName");
                String password = csvRecord.get("password");
                String email = csvRecord.get("email");
                Boolean isSystemUser = Boolean.valueOf(csvRecord.get("isSystemUser"));

                if (userName == null || userName.isEmpty() ||
                        password == null || password.isEmpty() ||
                        email == null || email.isEmpty() ||
                        isSystemUser == null
                ) {
                    throw new IllegalArgumentException("Doldurulması gerekli alanlar boş bırakılmıştır: " + csvRecord.toString());
                }
                User user;
                if (id != null && !id.isEmpty()) {
                    Optional<User> existingUser = userRepository.findById(Long.parseLong(id));
                    user = existingUser.orElse(new User());
                } else {
                    user = new User();
                }
                user.setUserName(csvRecord.get("userName"));
                user.setPassword(csvRecord.get("password"));
                user.setEmail(csvRecord.get("email"));
                user.setSystemUser(Boolean.parseBoolean(csvRecord.get("isSystemUser")));
                users.add(user);
            }

            userRepository.saveAll(users);
        }
    }

    public ByteArrayResource exportUsersToCSV() throws Exception {
        List<User> users = userRepository.findAll();

        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id", "userName", "email", "password", "userStatus", "birthdate"));

        for (User user : users) {
            csvPrinter.printRecord(user.getId(),user.getUserName(), user.getEmail(), user.getPassword(),user.getUserStatus(),user.getBirthDate());
        }

        csvPrinter.flush();
        csvPrinter.close();

        return new ByteArrayResource(writer.toString().getBytes());
    }

    public void saveRolesFromCSV(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            List<Role> roles = new ArrayList<>();

            for (CSVRecord csvRecord : csvParser) {
                String id = csvRecord.get("id");
                String roleName  = csvRecord.get("roleName");
                String shortCode = csvRecord.get("shortCode");
                String description = csvRecord.get("description");

                if (roleName == null || roleName.isEmpty() ||
                        shortCode == null || shortCode.isEmpty() ||
                        description == null || description.isEmpty()
                ) {
                    throw new IllegalArgumentException("Doldurulması gerekli alanlar boş bırakılmıştır: " + csvRecord.toString());
                }
                Role role;
                if (id != null && !id.isEmpty()) {
                    Optional<Role> existingRole = roleRepository.findById(Long.parseLong(id));
                    role = existingRole.orElse(new Role());
                } else {
                    role = new Role();
                }
                role.setRoleName(csvRecord.get("roleName"));
                role.setShortCode(csvRecord.get("shortCode"));
                role.setDescription(csvRecord.get("description"));
                roles.add(role);
            }

            roleRepository.saveAll(roles);
        }
    }

    public ByteArrayResource exportRolesToCSV() throws Exception {
        List<Role> roles = roleRepository.findAll();

        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id", "roleName", "description"));

        for (Role role : roles) {
            csvPrinter.printRecord(role.getId(),role.getShortCode(), role.getRoleName(), role.getDescription());
        }

        csvPrinter.flush();
        csvPrinter.close();

        return new ByteArrayResource(writer.toString().getBytes());
    }
}
