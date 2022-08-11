package com.project.studytogether.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SignUpRequestDto {


    @Email
    private String email;
    @NotBlank
    private String id;
    @NotBlank
    private String password;

}
