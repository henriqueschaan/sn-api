package com.snapi.domain;

import com.snapi.dto.post.PostCreationDTO;
import com.snapi.dto.post.PostUpdateDTO;
import com.snapi.repository.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "Post")
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean deleted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private String title;

    private String description;

    private String picLink;

    private String vidLink;

    private Boolean isPrivate = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int likesCount;

    public Post(PostCreationDTO data, UserRepository repository, Long userId) {
        this.title = data.title();
        this.description = data.description();
        this.picLink = data.picLink();
        this.vidLink = data.vidLink();
        this.user = repository.getReferenceById(userId);
    }

    public void updateInfo(PostUpdateDTO data) {

        if (data.title() != null) {
            this.title = data.title();
        }

        if (data.description() != null) {
            this.description = data.description();
        }

        if (data.picLink() != null) {
            this.picLink = data.picLink();
        }

        if (data.vidLink() != null) {
            this.vidLink = data.vidLink();
        }

        if (data.isPrivate() != null) {
            this.isPrivate = data.isPrivate();
        }

    }
    
    public void delete() {this.deleted = true;}

}
