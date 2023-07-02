package com.snapi.repository;

import com.snapi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdAndIsPrivateFalseAndDeletedFalse(Long userId);
    List<Post> findByUserIdAndDeletedFalse(Long userId);
}
