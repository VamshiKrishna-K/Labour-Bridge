package com.lb.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
    @NotBlank(message="Username is required")
    @Size(min=3,message="Minimum 3 characters is mandatory")
    private String name;

    @Email(message="Invalid email")
    private String email;

    @NotBlank(message="Phone number is required")
    @Size(min=10,max=10,message="Invalid phone")
    private String phone;
    
    private String role;
    @Size(min=6, message="Minimum 6 characters is mandatory")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
    message = "Password must contain 1 uppercase, 1 lowercase, 1 digit, 1 special character")
     private String password;

    private String repeatPassword;

    
}
