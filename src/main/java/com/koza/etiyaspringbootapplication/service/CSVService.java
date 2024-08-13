package com.koza.etiyaspringbootapplication.service;


import com.koza.etiyaspringbootapplication.annotations.Exportable;
import com.koza.etiyaspringbootapplication.annotations.Importable;
import com.koza.etiyaspringbootapplication.entity.Role;
import com.koza.etiyaspringbootapplication.entity.User;
import com.koza.etiyaspringbootapplication.entity.UserStatus;
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
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CSVService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void saveUsersFromCSV(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            List<User> users = new ArrayList<>();
            List<Field> importableFields = getImportableFields(User.class);

            for (CSVRecord csvRecord : csvParser) {
                String id = csvRecord.get("id");

                User user;
                if (id != null && !id.isEmpty()) {
                    Optional<User> existingUser = userRepository.findById(Long.parseLong(id));
                    user = existingUser.orElse(new User());
                } else {
                    user = new User();
                }

                for (Field field : importableFields) {
                    field.setAccessible(true);
                    String value = csvRecord.get(field.getName());

                    if (value != null && !value.isEmpty()) {
                        if (field.getType().equals(String.class)) {
                            field.set(user, value);
                        } else if (field.getType().equals(LocalDateTime.class)) {
                            field.set(user, LocalDateTime.parse(value));
                        } else if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                            field.set(user, Boolean.parseBoolean(value));
                        } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                            field.set(user, Long.parseLong(value));
                        }
                    }
                }

                String roles = csvRecord.get("roles");
                if (roles != null && !roles.isEmpty()) {
                    List<String> roleNames = Arrays.asList(roles.split(","));
                    for (String roleName : roleNames) {
                        Role role = saveRoleIfNotExists(roleName.trim());
                        user.getRoles().add(role);
                    }
                    user.setSystemUser(true);
                } else {
                    user.setSystemUser(false);
                }

                user.setUserStatus(UserStatus.CREATED);
                users.add(user);
            }

            userRepository.saveAll(users);
        }
    }
    private List<Field> getImportableFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Importable.class))
                .collect(Collectors.toList());
    }

    public Role saveRoleIfNotExists(String roleName) {
        Optional<Role> existingRole = roleRepository.findByRoleName(roleName.trim());
        if (existingRole.isPresent()) {
            return existingRole.get();
        } else {
            Role newRole = new Role();
            newRole.setRoleName(roleName.trim());
            return roleRepository.save(newRole);
        }
    }

    public void saveRolesFromCSV(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            List<Role> roles = new ArrayList<>();

            String[] headers = getImportableFieldNames(Role.class);

            for (CSVRecord csvRecord : csvParser) {

                Role role = new Role();

                for (String header : headers) {
                        String value = csvRecord.get(header);
                        Field field = getFieldByName(Role.class, header);
                        if (field != null && value != null && !value.isEmpty()) {
                            field.setAccessible(true);
                            if (field.getType().equals(String.class)) {
                                field.set(role, value);
                            } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                                field.set(role, Long.parseLong(value));
                            }
                        }
                }
                roles.add(role);
            }

            roleRepository.saveAll(roles);
        }
    }
    private String[] getImportableFieldNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Importable.class))
                .map(Field::getName)
                .toArray(String[]::new);
    }


    public ByteArrayResource exportRolesToCSV(String tableName) throws Exception {
        List<Role> roles = roleRepository.findAll();

        StringWriter writer = new StringWriter();
        String[] headers = getExportableFieldNames(Role.class); // Anotasyonla belirlenen alanları alıyoruz
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers));

        for (Role role : roles) {
            List<Object> recordValues = new ArrayList<>();
            for (String header : headers) {
                Field field = getFieldByName(Role.class, header);
                if (field != null) {
                    field.setAccessible(true);
                    recordValues.add(field.get(role));
                }
            }
            csvPrinter.printRecord(recordValues);
        }

        csvPrinter.flush();
        csvPrinter.close();

        return new ByteArrayResource(writer.toString().getBytes());
    }

    public ByteArrayResource exportUsersToCSV(String tableName) throws Exception {
        List<User> users = userRepository.findAll();

        StringWriter writer = new StringWriter();
        // Header alanlarını alıyoruz
        String[] headers = getExportableFieldNames(User.class);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers));

        for (User user : users) {
            List<Object> recordValues = new ArrayList<>();
            for (String header : headers) {
                Field field = getFieldByName(User.class, header);
                if (field != null) {
                    field.setAccessible(true);
                    if (field.getName().equals("roles")) {
                        String roles = user.getRoles().stream()
                                .map(Role::getRoleName)
                                .collect(Collectors.joining(","));
                        recordValues.add(roles);
                    } else {
                        recordValues.add(field.get(user));
                    }
                }
            }
            csvPrinter.printRecord(recordValues);
        }

        csvPrinter.flush();
        csvPrinter.close();

        return new ByteArrayResource(writer.toString().getBytes());
    }

    private String[] getExportableFieldNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Exportable.class))
                .map(Field::getName)
                .toArray(String[]::new);
    }

    private Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
