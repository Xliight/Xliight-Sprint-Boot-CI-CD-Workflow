package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.Category;
import com.ouitrips.app.entities.circuits.CircuitGroup;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.CircuitGroupMapper;
import com.ouitrips.app.repositories.security.UserRepository;
import com.ouitrips.app.repositories.security.circuits.CategoryRepository;
import com.ouitrips.app.repositories.security.circuits.CircuitGroupRepository;
import com.ouitrips.app.services.circuits.ICircuitGroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
@Service
public class CircuitGroupServiceImpl implements ICircuitGroupService {
    private final CircuitGroupRepository circuitGroupRepository;
    private final CircuitGroupMapper circuitGroupMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Object save(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        String name = (String) params.get("name");
        String description = (String) params.get("description");
        String color = (String) params.get("color");
        String icon = (String) params.get("icon");
        Integer userReference = (Integer) params.get("userReference");
        Integer categoryReference = (Integer) params.get("categoryReference");
        CircuitGroup circuitGroup;
        if (id == null) {
            circuitGroup = new CircuitGroup();
        } else {
            circuitGroup = this.getById(id);
        }
        if (name != null) {
            circuitGroup.setName(name);
        }
        if (description != null) {
            circuitGroup.setDescription(description);
        }
        if (color != null) {
            circuitGroup.setColor(color);
        }
        if (icon != null) {
            circuitGroup.setIcon(icon);
        }
        if (userReference != null) {
            User user = userRepository.findById(userReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("User with ID " + userReference + " not found"));
            circuitGroup.setUser(user);
        }
        if (categoryReference != null) {
            Category category = categoryRepository.findById(categoryReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("Category with ID " + categoryReference + " not found"));
            circuitGroup.setCategory(category);
        }

        return Map.of("reference", circuitGroupRepository.save(circuitGroup).getId());
    }
    public CircuitGroup addDefaultByUser(User user) {
        CircuitGroup defaultGroup = new CircuitGroup();
        defaultGroup.setName("Default Group");
        defaultGroup.setIsDefault(true);
        defaultGroup.setDescription("This is the default circuit group");
        defaultGroup.setColor("#000000");
        defaultGroup.setIcon("default-icon");
        defaultGroup.setUser(user);

        return circuitGroupRepository.save(defaultGroup);
    }
    @Override
    public void delete(Integer id) {circuitGroupRepository.delete(this.getById(id));}
    @Override
    public Object get(Integer id) {return circuitGroupMapper.apply(this.getById(id));}
    @Override
    public List<Object> getAll() {return circuitGroupRepository.findAll().stream().map(circuitGroupMapper).toList();}
    public CircuitGroup getById(Integer id) {
        return circuitGroupRepository.findById(id).orElseThrow(
                () -> new ExceptionControllerAdvice.ObjectNotFoundException("CircuitGroup not found")
        );}


}
