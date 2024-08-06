package com.koza.etiyaspringbootapplication.entity;

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

    private String roleName;
    private String description;
    private String shortCode;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
   }
