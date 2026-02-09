package com.lb.entities;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mediator_id")
    private Mediator mediator;

    @ManyToOne
    @JoinColumn(name = "work_request_id")
    private WorkRequest workRequest;

   
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentProgress paymentStatus;
}
