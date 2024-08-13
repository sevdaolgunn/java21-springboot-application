package com.koza.etiyaspringbootapplication.entity;

import com.koza.etiyaspringbootapplication.annotations.Exportable;
import com.koza.etiyaspringbootapplication.annotations.Importable;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role extends BaseEntity {

    @Exportable
    @Importable
    private String roleName;
    @Exportable
    @Importable
    private String description;
    @Exportable
    @Importable
    private String shortCode;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
   }
