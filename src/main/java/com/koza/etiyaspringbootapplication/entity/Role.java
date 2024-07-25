package com.koza.etiyaspringbootapplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String shortCode;
    private Date createDate;
    private Date updateDate;

   // @ManyToMany(mappedBy = "roles")
   // private List<User> users = new ArrayList<User>();
}
