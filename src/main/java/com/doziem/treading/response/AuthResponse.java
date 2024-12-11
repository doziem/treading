package com.doziem.treading.response;

import lombok.Data;

@Data
public class AuthResponse {

    private String token;
    private boolean status;
    private String message;
    private boolean isTwoFactoryAuthEnabled;
    private String session;
}
