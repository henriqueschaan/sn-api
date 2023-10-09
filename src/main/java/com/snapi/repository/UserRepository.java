package com.snapi.repository;

import com.snapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByUsername(String username);
    User findUserByUsername(String username);

    List<User> findByDeletedFalse();

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    @Query("SELECT u.friends FROM User u JOIN u.friends f WHERE u.id = :id AND f.deleted = false")
    List<User> findFriendsById(@Param("id") Long id);
}
