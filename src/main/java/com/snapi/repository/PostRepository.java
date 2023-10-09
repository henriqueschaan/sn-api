package com.snapi.repository;

import com.snapi.domain.Post;
import com.snapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdAndIsPrivateFalseAndDeletedFalseOrderByCreatedAtDesc(Long userId);
    List<Post> findByUserIdAndDeletedFalseOrderByCreatedAtDesc(Long userId);
    List<Post> findByUserInAndDeletedFalseOrderByCreatedAtDesc(List<User> users);

    List<Post> findByDeletedFalseAndUser_DeletedFalseOrderByCreatedAtDesc();

}
