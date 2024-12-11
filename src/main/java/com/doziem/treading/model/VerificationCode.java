package com.doziem.treading.model;

import com.doziem.treading.domain.VerificationType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class VerificationCode {

    @Id
    private Long id;

    private String otp;

    @OneToOne
    private Users user;

    private String email;

    private String mobile;

    private VerificationType verificationType;
}
