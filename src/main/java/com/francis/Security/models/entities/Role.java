package com.francis.Security.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    private String name;
}
