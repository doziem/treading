package com.doziem.treading.controller;

import com.doziem.treading.config.JwtProvider;
import com.doziem.treading.model.TwoFactorOTP;
import com.doziem.treading.model.Users;
import com.doziem.treading.repository.UserRepository;
import com.doziem.treading.request.LoginRequest;
import com.doziem.treading.response.AuthResponse;
import com.doziem.treading.service.CustomUserDetail;
import com.doziem.treading.service.EmailService;
import com.doziem.treading.service.ITwoFactorOtpService;
import com.doziem.treading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetail  customUserDetail;

    @Autowired
    private ITwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/home")
    public String homePage(){
        return "Welcome to Home Page";
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody Users user) throws Exception {

        Users existingUser = userRepository.findByEmail(user.getEmail());

        if(existingUser != null){
            throw new Exception("Email already Exist!");
        }

        Users newUser = new Users();

        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(user.getPassword());
        newUser.setMobile(user.getMobile());

        Users savedUser = userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setToken(jwt);
        res.setStatus(true);
        res.setMessage("Register success");

        return new ResponseEntity<>(res,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request) throws Exception {

        String username = request.getEmail();
        String password = request.getPassword();


        Authentication authentication = authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse();

        Users authUser = userRepository.findByEmail(username);

        if (authUser.getTwoFactorAuth().isEnabled()){
            res.setMessage("Two factor authentication enabled");
            res.setTwoFactoryAuthEnabled(true);

            String otp = OtpUtils.generateOtp();

            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(authUser.getId());
            if(oldTwoFactorOTP !=null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }

            TwoFactorOTP newTwoFactorOTP = twoFactorOtpService.createTwoFactorOtp(authUser,otp,jwt);

            emailService.sendVerificationOtpEmail(username,otp);

            res.setSession(newTwoFactorOTP.getId());
            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);

        }
        res.setToken(jwt);
        res.setStatus(true);
        res.setMessage("login success");

        return new ResponseEntity<>(res,HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetail.loadUserByUsername(username);

        if (userDetails == null){
            throw new BadCredentialsException("Invalid email or password");
        }
        if (!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Credentials");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());

    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable String otp,@RequestParam String id) throws Exception {

        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);

        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP,otp)){
            AuthResponse response = new AuthResponse();
            response.setMessage("Two factor authentication verified");
            response.setTwoFactoryAuthEnabled(true);
            response.setToken(twoFactorOTP.getToken());
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        throw new Exception("Invalid Otp");
    }
}
