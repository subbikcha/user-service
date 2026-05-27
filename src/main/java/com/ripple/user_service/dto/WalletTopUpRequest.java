package com.ripple.user_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletTopUpRequest {
    private Double amount;
}
