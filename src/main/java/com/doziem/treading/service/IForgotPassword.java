package com.doziem.treading.service;

import com.doziem.treading.domain.VerificationType;
import com.doziem.treading.model.ForgotPasswordToken;
import com.doziem.treading.model.Users;

public interface IForgotPassword {

    ForgotPasswordToken createToken(Users user, String id, String otp, VerificationType verificationType, String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);
}
