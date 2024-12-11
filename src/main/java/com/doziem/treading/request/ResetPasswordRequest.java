package com.doziem.treading.request;

import com.doziem.treading.domain.VerificationType;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String otp;
    private String password;
}
