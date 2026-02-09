package com.lb.forms;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LabourForm {

    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Minimum 3 characters are required for name")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Minimum age must be 18")
    @Max(value = 65, message = "Maximum age allowed is 65")
    private Integer age;

    @NotBlank(message = "Phone number required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    
    @NotNull(message = "Daily wage is required")
    @DecimalMin(value = "100.00", message = "Daily wage must be at least ₹100")
    @Digits(integer = 8, fraction = 2, message = "Invalid wage format (e.g., 500.00)")
    private BigDecimal dailyWage;
    
  
}
