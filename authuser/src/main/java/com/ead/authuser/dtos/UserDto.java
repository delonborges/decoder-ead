package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String username;
    private String password;
    private String oldPassword;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;
}
