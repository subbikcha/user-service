package com.ripple.user_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    private String userId;

    private String userName;

    private String tier;

    private Integer rewardPoints;

    private String email;

    private String phoneNumber;

    private String address;

    @Builder.Default
    private Double walletBalance = 0.0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder.Default
    private Boolean isActive = true;

    public Double getWalletBalance() {
        return this.walletBalance == null ? null : this.walletBalance * 100;
    }
}
