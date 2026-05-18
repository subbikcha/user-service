package com.ripple.user_service.config;


import com.ripple.user_service.entity.User;
import com.ripple.user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner load(UserRepository repo) {
        return args -> {

            User user = new User();

            user.setUserId("u1");
            user.setUserName("John");
            user.setTier("premium");
            user.setRewardPoints(120);

            repo.save(user);
        };
    }
}