package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.TransportMeans;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.TransportMeansMapper;
import com.ouitrips.app.repositories.security.circuits.TransportMeansRepository;
import com.ouitrips.app.services.circuits.ITransportMeansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class TransportMeansServiceImp implements ITransportMeansService {
    private final TransportMeansRepository transportMeansRepository;
    private final TransportMeansMapper transportMeansMapper;

    @Override
    public Object save(Map<String, Object> params) {
        String name = (String) params.get("name");
        String code = (String) params.get("code");
        Integer id = (Integer) params.get("id");
        TransportMeans transportMeans;
        if (id == null) {
            transportMeans = new TransportMeans();
        } else {
            transportMeans = this.getById(id);
        }
        if (name != null){
            transportMeans.setName(name);
        }
        if (code != null) {
            transportMeans.setCode(code);
        }
        return Map.of("reference", transportMeansRepository.save(transportMeans).getId());
    }

    @Override
    public void delete(Integer id) {
        transportMeansRepository.delete(this.getById(id));
    }
    @Override
    public Object get(Integer id) {
        return transportMeansMapper.apply(this.getById(id));
    }
    @Override
    public Object getAll() {
        return transportMeansRepository.findAll().stream().map(transportMeansMapper).toList();
    }

    public TransportMeans getById(Integer id) {
        return transportMeansRepository.findById(id).orElseThrow(
                ()->new ExceptionControllerAdvice.ObjectNotFoundException("Mode Of Transport not found")
        );
    }
}
