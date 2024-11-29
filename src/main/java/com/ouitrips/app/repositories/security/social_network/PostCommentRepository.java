package com.ouitrips.app.repositories.security.social_network;

import com.ouitrips.app.entities.social_network.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Integer>{
}
