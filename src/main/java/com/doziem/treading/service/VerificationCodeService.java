package com.doziem.treading.service;

import com.doziem.treading.domain.VerificationType;
import com.doziem.treading.model.Users;
import com.doziem.treading.model.VerificationCode;
import com.doziem.treading.repository.VerificationCodeRepository;
import com.doziem.treading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeService implements IVerificationCodeService{

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode sendVerificationCode(Users user, VerificationType verificationType) {
        VerificationCode verificationCode = new VerificationCode();

        verificationCode.setVerificationType(verificationType);
        verificationCode.setUser(user);
        verificationCode.setOtp(OtpUtils.generateOtp());

        return verificationCodeRepository.save(verificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificationCode = Optional.ofNullable(verificationCodeRepository.findByUserId(id));
        if(verificationCode.isPresent()){
            return verificationCode.get();
        }
        throw new Exception("Verification Code not found");
    }

    @Override
    public VerificationCode getVerificationByUserId(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCode(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }
}
