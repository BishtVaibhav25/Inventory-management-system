package com.ims.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<User, Long> means:
//   User = the entity this repository manages
//   Long = the type of the @Id field
//
// Spring auto-generates these methods for FREE:
//   findAll(), findById(Long), save(User), deleteById(Long), count(), existsById(Long)
//
// We add custom methods below — Spring reads the method NAME and generates the SQL!

public interface UserRepository extends JpaRepository<User, Long> {

    // Spring generates: SELECT * FROM users WHERE username = ?
    // Returns Optional because the user might not exist
    Optional<User> findByUsername(String username);

    // Spring generates: SELECT COUNT(*) > 0 FROM users WHERE username = ?
    boolean existsByUsername(String username);
}