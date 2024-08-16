package com.koza.etiyaspringbootapplication.service;


import com.koza.etiyaspringbootapplication.config.util.ReflectionHelper;
import com.koza.etiyaspringbootapplication.entity.Role;
import com.koza.etiyaspringbootapplication.entity.User;
import com.koza.etiyaspringbootapplication.repository.RoleRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CSVService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ReflectionHelper reflectionHelper;

    public void saveEntitiesFromCSV(String tableName, MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            // Entity sınıfını tableName üzerinden buluyoruz
            Class<?> entityClass = reflectionHelper.findEntityClassByTableName(tableName);
            List<Object> entities = new ArrayList<>();

            // Importable alanları bul
            String[] headers = reflectionHelper.getImportableFieldNames(entityClass);

            for (CSVRecord csvRecord : csvParser) {
                Object entity = entityClass.getDeclaredConstructor().newInstance();

                for (String header : headers) {
                    String value = csvRecord.get(header);
                    Field field = reflectionHelper.getFieldByName(entityClass, header);

                    if (field != null && value != null && !value.isEmpty()) {
                        field.setAccessible(true);

                        if (field.getType().equals(String.class)) {
                            field.set(entity, value);
                        } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                            field.set(entity, Long.parseLong(value));
                        } else if (field.getType().equals(Set.class)) {
                            if (entity instanceof User) {
                                // User için roller kontrolü
                                Set<Role> roles = new HashSet<>();
                                String rolesString = csvRecord.get("roles");
                                if (rolesString != null && !rolesString.isEmpty()) {
                                    String[] roleNames = rolesString.split(",");
                                    for (String roleName : roleNames) {
                                        Role role = findOrSaveRole(roleName.trim());
                                        roles.add(role);
                                    }
                                    ((User) entity).setRoles(roles);
                                    ((User) entity).setSystemUser(true);
                                } else {
                                    ((User) entity).setSystemUser(false);
                                }
                            }
                        }
                    }
                }
                entities.add(entity);
            }

            // Repository'yi entity class üzerinden bul ve kaydet
            JpaRepository<Object, ?> repository = (JpaRepository<Object, ?>) reflectionHelper.findRepositoryForEntity(entityClass);
            repository.saveAll(entities);
        }
    }
    private Role findOrSaveRole(String roleName) { //roleservice
        Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);
        Role role;
        if (optionalRole.isPresent()) {
            role = optionalRole.get();
        } else {
            role = new Role();
            role.setRoleName(roleName);
            roleRepository.save(role);
        }
        return role;
    }

    public <T> ByteArrayResource exportEntitiesToCSV(String tableName) throws Exception {
        // Entity sınıfını tableName üzerinden bul
        Class<T> entityClass = reflectionHelper.findEntityClassByTableName(tableName);
        List<T> entities = reflectionHelper.findAllEntities(entityClass);

        StringWriter writer = new StringWriter();
        String[] headers = reflectionHelper.getExportableFieldNames(entityClass);
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers));

        for (T entity : entities) {
            List<Object> recordValues = new ArrayList<>();
            for (String header : headers) {
                Field field = reflectionHelper.getFieldByName(entityClass, header);
                if (field != null) {
                    field.setAccessible(true);
                    Object value = field.get(entity);

                    if (value != null) {
                        if (value instanceof Set<?>) {
                            // Eğer field Set türündeyse ve içindeki nesneleri işleyelim
                            Set<?> set = (Set<?>) value;
                            String joinedValues = set.stream()
                                    .map(relatedEntity -> {
                                        try {
                                            // İlişkili nesnenin Exportable alanını almak için
                                            return reflectionHelper.getExportableFieldValue(relatedEntity);
                                        } catch (Exception e) {
                                            return "";
                                        }
                                    })
                                    .collect(Collectors.joining(","));
                            recordValues.add(joinedValues);
                        } else {
                            recordValues.add(value.toString());
                        }
                    } else {
                        recordValues.add("");
                    }
                }
            }
            csvPrinter.printRecord(recordValues);
        }

        csvPrinter.flush();
        csvPrinter.close();

        return new ByteArrayResource(writer.toString().getBytes());
    }
}
