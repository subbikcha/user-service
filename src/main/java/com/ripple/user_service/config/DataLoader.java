package com.ripple.user_service.config;

import com.ripple.user_service.entity.User;
import com.ripple.user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner load(UserRepository repo) {
        return args -> {
            if (repo.count() > 0) return;

            repo.saveAll(List.of(
                User.builder().userId("u1").userName("John").tier("premium")
                    .rewardPoints(120).email("john@quickbite.com")
                    .phoneNumber("+91-9876543210").address("12 MG Road, Bangalore")
                    .walletBalance(500.0).isActive(true).build(),

                User.builder().userId("u2").userName("Sarah").tier("premium")
                    .rewardPoints(350).email("sarah@quickbite.com")
                    .phoneNumber("+91-9123456789").address("45 Bandra West, Mumbai")
                    .walletBalance(1200.0).isActive(true).build(),

                User.builder().userId("u3").userName("Mike").tier("standard")
                    .rewardPoints(45).email("mike@quickbite.com")
                    .phoneNumber("+91-9988776655").address("7 Connaught Place, Delhi")
                    .walletBalance(200.0).isActive(true).build(),

                User.builder().userId("u4").userName("Emma").tier("standard")
                    .rewardPoints(0).email("emma@quickbite.com")
                    .phoneNumber("+91-8877665544").address("22 Anna Nagar, Chennai")
                    .walletBalance(0.0).isActive(true).build(),

                User.builder().userId("u5").userName("Alex").tier("new")
                    .rewardPoints(10).email("alex@quickbite.com")
                    .phoneNumber("+91-7766554433").address("33 Park Street, Kolkata")
                    .walletBalance(50.0).isActive(true).build()
            ));
        };
    }
}
