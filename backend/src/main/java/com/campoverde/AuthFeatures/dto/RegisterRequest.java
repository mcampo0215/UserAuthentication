package com.campoverde.AuthFeatures.dto;

import lombok.Data;

/*entire class represents data coming in from the front end (unrelated to database model) */
@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
}
