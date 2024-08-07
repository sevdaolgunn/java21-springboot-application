package com.koza.etiyaspringbootapplication.entity;

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

    @Column(length = 250)
    private String userName;
    private String password;
    @Column(length = 250)
    private String email;
    private boolean isSystemUser;
    @Enumerated(EnumType.STRING)
    @Column(name = "userStatus")
    private UserStatus userStatus;
    private LocalDateTime birthDate;


    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
