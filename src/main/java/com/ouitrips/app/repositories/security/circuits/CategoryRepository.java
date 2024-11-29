package com.ouitrips.app.repositories.security.circuits;

import com.ouitrips.app.entities.circuits.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
