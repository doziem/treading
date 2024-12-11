package com.doziem.treading.service;

import com.doziem.treading.config.JwtProvider;
import com.doziem.treading.domain.VerificationType;
import com.doziem.treading.model.TwoFactorAuth;
import com.doziem.treading.model.Users;
import com.doziem.treading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Users findUserByProfileJwt(String token) throws Exception {
        String email = JwtProvider.getEmailFromToken(token);

        Users user = userRepository.findByEmail(email);

        if(user == null){
            throw new Exception("User not found");
        }

        return user;
    }

    @Override
    public Users findUserByEmail(String email) throws Exception {
        Users user = userRepository.findByEmail(email);

        if(user == null){
            throw new Exception("User not found");
        }
        return user;
    }

    @Override
    public Users findUserById(Long id) throws Exception {
        Optional<Users> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new Exception("User not found");
        }
        return user.get();
    }

    @Override
    public Users enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, Users user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

        twoFactorAuth.setSendTo(verificationType);
        twoFactorAuth.setEnabled(true);

        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }

    @Override
    public Users updatePassword(Users user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
