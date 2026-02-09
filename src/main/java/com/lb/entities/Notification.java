package com.lb.entities;

import jakarta.persistence.*;
import lombok.*;
/* import java.time.LocalDateTime; */

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Lob
    private String message;
/* 
    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean readStatus = false; */
}
