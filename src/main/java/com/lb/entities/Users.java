package com.lb.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // BIGINT (Primary Key)

    @Column(nullable = false, length = 20)
    private String name; 

    @Column(nullable = false, unique = true, length = 30)
    private String email; 

    @Column(nullable = false, length = 6)
    private String password; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role; 

    @Column(length = 10,nullable = false)
    private String phone; 
 
    @Column(name = "profilepic", length = 255)
    private String profilePic; 
 
     @Column(nullable = false)
    public boolean enabled; 

    @Column(nullable = false)
    private boolean emailVerified;
}
