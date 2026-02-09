package com.lb.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "constructors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Constructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // independent primary key

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Users user;  // foreign key reference to Users
}
