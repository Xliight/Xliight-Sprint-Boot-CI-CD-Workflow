package com.ouitrips.app.mapper.security.circuits;

import com.ouitrips.app.entities.circuits.LinksLs;
import com.ouitrips.app.entities.circuits.Step;
import com.ouitrips.app.repositories.security.circuits.LinksLsRepository;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class StepsMapper implements Function<Step, Object> {
    private final LinksLsRepository linksLsRepository;
    private final LocationsMapper locationsMapper;
    private final TransportMeansMapper transportMeansMapper;

    public StepsMapper(LinksLsRepository linksLsRepository, LocationsMapper locationsMapper, TransportMeansMapper transportMeansMapper) {
        this.linksLsRepository = linksLsRepository;
        this.locationsMapper = locationsMapper;
        this.transportMeansMapper = transportMeansMapper;
    }

    @Override
    public Object apply(Step steps) {
        Map<String, Object> response = new HashMap<>();
        if(steps == null)
            return response;
        response.put("reference", steps.getId());
        response.put("name", steps.getName());
        response.put("directions", steps.getDirections());
        response.put("order_step", steps.getOrderStep());
        response.put("state", steps.getState());
        List<LinksLs> links = linksLsRepository.findByStep(steps);
        if(!links.isEmpty()){
            LinksLs link = links.get(0);
            response.put("duration", link.getDuration());
            response.put("distance", link.getDistance());
            if (link.getLocationStart() != null)
                response.put("location_info", locationsMapper.apply(link.getLocationStart()));
            if (link.getMode() != null)
                response.put("transport_means", transportMeansMapper.apply(link.getMode()));
        }

        return response;
    }
}
