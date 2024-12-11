package com.doziem.treading.controller;

import com.doziem.treading.domain.VerificationType;
import com.doziem.treading.model.ForgotPasswordToken;
import com.doziem.treading.model.Users;
import com.doziem.treading.model.VerificationCode;
import com.doziem.treading.request.ResetPasswordRequest;
import com.doziem.treading.response.ApiResponse;
import com.doziem.treading.response.AuthResponse;
import com.doziem.treading.service.*;
import com.doziem.treading.request.ForgotPasswordTokenRequest;
import com.doziem.treading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private IForgotPassword forgotPasswordService;
    private  String token;

    @GetMapping("/api/users/profile")
    public ResponseEntity<Users> getUserProfile(@RequestHeader("Authorization") String token) throws Exception {

        Users user = userService.findUserByProfileJwt(token);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String token,
                                                     @PathVariable VerificationType verificationType) throws Exception {

//        this.token = token;
        Users user = userService.findUserByProfileJwt(token);

        VerificationCode verificationCode = verificationCodeService.getVerificationByUserId(user.getId());

        if(verificationCode != null){
           verificationCode = verificationCodeService.sendVerificationCode(user,verificationType);
        }

        if(verificationType.equals(VerificationType.EMAIL)){
            assert verificationCode != null;
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }

        return new ResponseEntity<>("Verification otp sent successfully", HttpStatus.OK);
    }



    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<Users> enableTwoFactorAuthentication(
            @PathVariable String otp,
            @RequestHeader("Authorization") String token) throws Exception {

        Users user = userService.findUserByProfileJwt(token);

        VerificationCode verificationCode = verificationCodeService.getVerificationByUserId(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)?
                verificationCode.getEmail():verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if(isVerified){
            Users updatedUser = userService.enableTwoFactorAuthentication(
                    verificationCode.getVerificationType(),sendTo,user);

            verificationCodeService.deleteVerificationCode(verificationCode);
            throw new Exception("Wrong otp");

        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(@RequestBody ForgotPasswordTokenRequest req) throws Exception {

        Users user = userService.findUserByEmail(req.getSendTo());

        String otp = OtpUtils.generateOtp();

        UUID uuid = UUID.randomUUID();

        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());

        if(token == null){
          token = forgotPasswordService.createToken(user,id,otp,req.getVerificationType(),req.getSendTo());

        }

        if(req.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(),token.getOtp());
        }

        AuthResponse response = new AuthResponse();

        response.setSession(token.getId());
        response.setMessage("forgot password otp sent successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest req,
            @RequestHeader("Authorization") String token) throws Exception {

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp());

        if (isVerified) {
            userService.updatePassword(forgotPasswordToken.getUser(),req.getPassword());
            ApiResponse res = new ApiResponse();
            res.setMessage("Password updated successfully");

            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
        }

    throw new Exception("Wrong otp");
    }
}
