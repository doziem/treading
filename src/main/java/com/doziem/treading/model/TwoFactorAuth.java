package com.doziem.treading.model;

import com.doziem.treading.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;

    private VerificationType sendTo;

}
