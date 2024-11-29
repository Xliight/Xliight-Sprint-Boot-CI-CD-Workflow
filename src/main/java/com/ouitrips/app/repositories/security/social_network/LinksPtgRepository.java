package com.ouitrips.app.repositories.security.social_network;

import com.ouitrips.app.entities.social_network.LinksPtg;
import com.ouitrips.app.entities.social_network.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinksPtgRepository extends JpaRepository<LinksPtg, Integer> {
    List<LinksPtg> findByPost(Post post);
}
