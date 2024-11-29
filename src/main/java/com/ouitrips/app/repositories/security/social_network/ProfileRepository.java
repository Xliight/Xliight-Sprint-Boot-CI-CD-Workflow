package com.ouitrips.app.repositories.security.social_network;

import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.entities.social_network.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProfileRepository extends JpaRepository<Profile, Integer>, JpaSpecificationExecutor<Profile> {
    Profile findByUser(User userConnected);
}
