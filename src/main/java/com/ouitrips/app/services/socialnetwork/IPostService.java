package com.ouitrips.app.services.socialnetwork;


import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

public interface IPostService {
    Object save(Map<String, Object> params);
    void delete(Integer id);
    List<Object> getByFollower();
    List<Object> getByProfile();
    Object get(Integer id);
    @Transactional
    List<Object> getPostsByProfile(Integer profileId);
    Object getAll();
}
