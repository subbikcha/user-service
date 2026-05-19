package com.ripple.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "users1234")
@Setter
@NoArgsConstructor
public class User {

    @Id
    private String userId;

    private String userName;

    private String tier;

    private Integer rewardPoints;
}