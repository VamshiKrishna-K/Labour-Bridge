package com.lb.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginForm {
     
    @NotBlank(message="Email is required")
    private String email;

    @NotBlank(message="Password is required")
    private String password;


}
