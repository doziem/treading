package com.doziem.treading.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
