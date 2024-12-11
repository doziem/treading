package com.doziem.treading.service;

import com.doziem.treading.domain.VerificationType;
import com.doziem.treading.model.Users;

public interface IUserService {
    public Users findUserByProfileJwt(String token) throws Exception;

    public Users findUserByEmail(String email) throws Exception;

    public Users findUserById(Long id) throws Exception;

    public Users enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, Users user);

    public Users updatePassword(Users user, String newPassword);

}
