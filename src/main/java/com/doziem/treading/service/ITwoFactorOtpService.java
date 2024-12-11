package com.doziem.treading.service;

import com.doziem.treading.model.TwoFactorOTP;
import com.doziem.treading.model.Users;

public interface ITwoFactorOtpService {

    TwoFactorOTP createTwoFactorOtp(Users user, String otp, String token);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);

    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);
}
