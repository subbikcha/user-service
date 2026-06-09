package com.ripple.user_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String userName;
    private String email;
    private String phoneNumber;
    private String address;
    private String tier;
    private Integer loyaltyPoints;
    private Double walletBalance;
}
