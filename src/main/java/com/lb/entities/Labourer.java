package com.lb.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;


@Entity
@Table(name = "labourers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Labourer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "m_id")
    private  Users mediator;
    
    @NotNull(message = "Name must  required")
    @Column(length = 100)
    private String name;
    
     @NotNull(message = "Age must  required")
    private Integer age;

    @Column(precision = 10, scale = 2)
    private BigDecimal dailyWage;
     
    @NotNull(message = "Phone number required")
     @Column(length = 10,nullable = false)
    private String phone; 
 
    

   
}
