package com.doziem.treading.service;

import com.doziem.treading.model.TwoFactorOTP;
import com.doziem.treading.model.Users;
import com.doziem.treading.repository.TwoFactorOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements ITwoFactorOtpService {

    @Autowired
    private TwoFactorOtpRepository twoFactorOtpRepository;

    @Override
    public TwoFactorOTP createTwoFactorOtp(Users user, String otp, String token) {
        UUID uuid = UUID.randomUUID();

        String id = uuid.toString();

        TwoFactorOTP twoFactorOTP = new TwoFactorOTP();

        twoFactorOTP.setId(id);
        twoFactorOTP.setToken(token);
        twoFactorOTP.setUser(user);
        twoFactorOTP.setOtp(otp);
        return twoFactorOtpRepository.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUser(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findById(String id) {
        Optional<TwoFactorOTP> otp = twoFactorOtpRepository.findById(id);
        return otp.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp) {
        return twoFactorOTP.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP) {

//     twoFactorOtpRepository.findById(id).ifPresentOrElse(twoFactorOtpRepository::delete,()->{throw new RuntimeException("Otp not found");});
        twoFactorOtpRepository.delete(twoFactorOTP);

    }
}
