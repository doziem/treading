package com.doziem.treading.service;

import com.doziem.treading.domain.VerificationType;
import com.doziem.treading.model.Users;
import com.doziem.treading.model.VerificationCode;

public interface IVerificationCodeService {
    VerificationCode sendVerificationCode(Users user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationByUserId(Long userId);

    void deleteVerificationCode(VerificationCode verificationCode);
}
