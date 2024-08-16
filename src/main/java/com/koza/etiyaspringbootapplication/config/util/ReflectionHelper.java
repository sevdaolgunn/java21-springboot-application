package com.koza.etiyaspringbootapplication.config.util;

import com.koza.etiyaspringbootapplication.annotations.Exportable;
import com.koza.etiyaspringbootapplication.annotations.Importable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
@Component
public class ReflectionHelper {
    @Autowired
    private ApplicationContext applicationContext;
    public String[] getExportableFieldNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Exportable.class))
                .map(Field::getName)
                .toArray(String[]::new);
    }

    public Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
    public String[] getImportableFieldNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Importable.class))
                .map(Field::getName)
                .toArray(String[]::new);
    }
    public  <T> List<T> findAllEntities(Class<T> entityClass) {
        JpaRepository<T, ?> repository = getRepositoryForEntity(entityClass);
        return repository.findAll();
    }

    public  <T> JpaRepository<T, ?> getRepositoryForEntity(Class<T> entityClass) {
        String repositoryBeanName = entityClass.getSimpleName().substring(0, 1).toLowerCase() + entityClass.getSimpleName().substring(1) + "Repository";
        return (JpaRepository<T, ?>) applicationContext.getBean(repositoryBeanName);
    }

    // entity sınıfı bulucu metod
    @SuppressWarnings("unchecked")
    public <T> Class<T> findEntityClassByTableName(String tableName) throws ClassNotFoundException {
        String entityClassName = "com.koza.etiyaspringbootapplication.entity." + tableName;
        return (Class<T>) Class.forName(entityClassName);
    }

    // repository bulucu metod
    @SuppressWarnings("unchecked")
    public  <T> JpaRepository<T, ?> findRepositoryForEntity(Class<T> clazz) {
        String entityName = clazz.getSimpleName();
        String repositoryBeanName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1) + "Repository";
        return (JpaRepository<T, ?>) applicationContext.getBean(repositoryBeanName);
    }

    public String getExportableFieldValue(Object entity) throws Exception {
        Class<?> entityClass = entity.getClass();
        String[] exportableFields = getExportableFieldNames(entityClass);
        if (exportableFields.length > 0) {
            Field field = getFieldByName(entityClass, exportableFields[0]);
            if (field != null) {
                field.setAccessible(true);
                Object value = field.get(entity);
                return value != null ? value.toString() : "";
            }
        }
        return "";
    }


}
