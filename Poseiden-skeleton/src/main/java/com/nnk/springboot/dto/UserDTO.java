package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @NotEmpty(message = "Username is mandatory")
    @NotBlank(message = "")
    private String username;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 8, message = "Password has to be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
            message = "Le mot de passe doit contenir au moins une lettre majuscule, et au moins un chiffre et un symbole")
    private String password;

    @NotEmpty(message = "FullName is mandatory")
    @NotBlank(message = "")
    private String fullname;

    @NotEmpty(message = "Role is mandatory")
    @NotBlank(message = "")
    private String role;

    public UserDTO(String username, String password, String fullname, String role) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.role = role;
    }

    public UserDTO(){}
}
