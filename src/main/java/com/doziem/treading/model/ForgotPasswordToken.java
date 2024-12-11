package com.doziem.treading.model;

import com.doziem.treading.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private Users user;

    private String otp;

    private VerificationType verificationType;

    private String sendTo;

}
