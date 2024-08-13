package com.koza.etiyaspringbootapplication.entity;

import com.koza.etiyaspringbootapplication.annotations.Exportable;
import com.koza.etiyaspringbootapplication.annotations.Importable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity{

    @Exportable
    @Importable
    private String userName;
    @Exportable
    @Importable
    private String password;
    @Exportable
    @Importable
    private String email;
    @Exportable
    private boolean isSystemUser;
    @Enumerated(EnumType.STRING)
    @Column(name = "userStatus")
    @Exportable
    @Importable
    private UserStatus userStatus;
    @Exportable
    @Importable
    private LocalDateTime birthDate;


    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Exportable
    @Importable
    private Set<Role> roles = new HashSet<>();
}
