package com.ims;

import com.ims.auth.Role;
import com.ims.auth.User;
import com.ims.auth.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ImsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImsBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (!userRepository.existsByUsername("admin")) {
				userRepository.save(User.builder()
						.username("admin")
						.name("Vaibhav Bisht")
						.password(passwordEncoder.encode("demo"))
						.role(Role.ADMIN)
						.build());
			}
			if (!userRepository.existsByUsername("manager")) {
				userRepository.save(User.builder()
						.username("manager")
						.name("Rahul Sharma")
						.password(passwordEncoder.encode("demo"))
						.role(Role.MANAGER)
						.build());
			}
			if (!userRepository.existsByUsername("staff")) {
				userRepository.save(User.builder()
						.username("staff")
						.name("Priya Singh")
						.password(passwordEncoder.encode("demo"))
						.role(Role.STAFF)
						.build());
			}
		};
	}
}

