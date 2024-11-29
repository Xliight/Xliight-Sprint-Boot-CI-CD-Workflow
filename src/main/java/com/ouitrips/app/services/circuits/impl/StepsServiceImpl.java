package com.ouitrips.app.services.circuits.impl;

import com.ouitrips.app.entities.circuits.*;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.StepsMapper;
import com.ouitrips.app.repositories.security.circuits.*;
import com.ouitrips.app.services.circuits.IStepsService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Slf4j
@AllArgsConstructor
@Service
public class StepsServiceImpl implements IStepsService {
    private final StepsRepository stepsRepository;
    private final StepsMapper stepsMapper;
    private final LinksLsRepository linksLsRepository;
    private final LocationsRepository locationRepository;
    private final TransportMeansRepository transportMeansRepository;
    private final CircuitRepository circuitRepository;
    private static final String ORDER_STEP = "orderStep";

    public boolean checkIfExists(Map<String, Object> params){
        Integer locationReference = (Integer) params.get("locationReference");
        Integer modeReference = (Integer) params.get("ModeReference");
        Float duration = (Float) params.get("duration");
        Float distance = (Float) params.get("distance");
        return locationReference != null ||
                modeReference != null ||
                duration != null ||
                distance != null;
    }
    @Override
    @Transactional
    public Object save(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        String directions = (String) params.get("directions");
        String name = (String) params.get("name");
        Integer orderStep = (Integer) params.get(ORDER_STEP);
        Boolean state = (Boolean) params.get("state");
        Float duration = (Float) params.get("duration");
        Float distance = (Float) params.get("distance");
        Integer circuitReference = (Integer) params.get("circuitReference");
        Integer locationReference = (Integer) params.get("locationReference");
        String location = (String) params.get("location");
        Integer locationEndReference = (Integer) params.get("LocationEndReference");
        Integer modeReference = (Integer) params.get("ModeReference");
        Step steps;
        LinksLs newLinksLs = null;
        LinksLs oldLinksLs = null;
        LinksLsId linksLsId = null;
        boolean updateLinks = this.checkIfExists(params);

        if (id == null) {
            steps = new Step();
            newLinksLs = new LinksLs();
            linksLsId = new LinksLsId();
        } else {
            steps = this.getById(id);
            oldLinksLs = steps.getLinks().iterator().next();
            if(updateLinks){
                newLinksLs = new LinksLs();
                newLinksLs.setStep(oldLinksLs.getStep());
                newLinksLs.setLocationStart(oldLinksLs.getLocationStart());
                newLinksLs.setLocationEnd(oldLinksLs.getLocationEnd());
                newLinksLs.setMode(oldLinksLs.getMode());
                newLinksLs.setDistance(oldLinksLs.getDistance());
                newLinksLs.setDuration(oldLinksLs.getDuration());
                linksLsId = new LinksLsId();
                linksLsId.setFkidStep(oldLinksLs.getId().getFkidStep());
                linksLsId.setFkidMode(oldLinksLs.getId().getFkidMode());
                log.info("Id end: {}", oldLinksLs.getId().getFkidLocationEnd());
                log.info("Id start: {}", oldLinksLs.getId().getFkidLocationStart());
                linksLsId.setFkidLocationStart(oldLinksLs.getId().getFkidLocationStart());
                linksLsId.setFkidLocationEnd(oldLinksLs.getId().getFkidLocationEnd());
            }
        }
        if (directions != null){steps.setDirections(directions);}
        if (name != null){steps.setName(name);}

        if (state != null){steps.setState(state);}
        Circuit circuit;
        if (circuitReference != null) {
            circuit = circuitRepository.findById(circuitReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("Circuit with ID " + circuitReference + " not found"));
            steps.setCircuit(circuit);
        }else{//Case update
            circuit = steps.getCircuit();
        }
        //Update mechanism order steps
        List<Step> stepsInCircuit = stepsRepository.findByCircuit(circuit, Sort.by(ORDER_STEP));
        if(id == null)
            setOrderStepForNewStep(steps, stepsInCircuit, orderStep);
        else setOrderStepForUpdatedStep(steps, stepsInCircuit, orderStep);
        //Save step
        stepsRepository.save(steps);
        if(updateLinks){
            newLinksLs.setStep(steps);
        }
        boolean wasUpdated = false;
        if(duration != null){
            newLinksLs.setDuration(duration);
            wasUpdated = true;
        }
        if(distance != null){
            newLinksLs.setDistance(distance);
            wasUpdated = true;
        }
        if (locationReference != null) {
            Location locationStart = locationRepository.findById(locationReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("Location with ID " + locationReference + " not found"));
            newLinksLs.setLocationStart(locationStart);
            linksLsId.setFkidLocationStart(locationReference);
            wasUpdated = true;
        }
        else if(location != null){
            Location existingLocation = locationRepository.findByName(location);
            if(existingLocation == null) {
                Location newLocation = new Location();
                newLocation.setName(location);
                locationRepository.save(newLocation);
                newLinksLs.setLocationStart(newLocation);
                linksLsId.setFkidLocationStart(newLocation.getId());
            } else {
                newLinksLs.setLocationStart(existingLocation);
                linksLsId.setFkidLocationStart(existingLocation.getId());
            }
            wasUpdated = true;
        }
        if (locationEndReference != null) {
            Location locationEnd = locationRepository.findById(locationEndReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("Location with ID " + locationEndReference + " not found"));
            newLinksLs.setLocationEnd(locationEnd);
            linksLsId.setFkidLocationEnd(locationEndReference);
            wasUpdated = true;
        } else if (newLinksLs != null) {
            newLinksLs.setLocationEnd(newLinksLs.getLocationStart());
            linksLsId.setFkidLocationEnd(newLinksLs.getLocationStart().getId());
        }
        if (modeReference != null) {
            TransportMeans mode = transportMeansRepository.findById(modeReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("TransportMeans with ID " + modeReference + " not found"));
            newLinksLs.setMode(mode);
            if(linksLsId != null)
                linksLsId.setFkidMode(modeReference);
            wasUpdated = true;
        }
        if(wasUpdated){
            linksLsId.setFkidStep(steps.getId());
            newLinksLs.setId(linksLsId);
            if(id != null){
                if(oldLinksLs != null) {
                    linksLsRepository.delete(oldLinksLs);
                }
            }
        }
        if(newLinksLs != null) {
            linksLsRepository.save(newLinksLs);
        }
        return Map.of("reference", steps.getId());
    }
    public void setOrderStepForNewStep(Step steps, List<Step> stepsInCircuit, Integer orderStep) {
        if (stepsInCircuit.isEmpty()) {
            steps.setOrderStep(1);
        } else {
            Step lastStep = stepsInCircuit.get(stepsInCircuit.size() - 1);
            if (orderStep == null || orderStep <= 0) {
                steps.setOrderStep(lastStep.getOrderStep() + 1);
            } else if (lastStep.getOrderStep() < orderStep) {
                steps.setOrderStep(lastStep.getOrderStep() + 1);
            } else {
                steps.setOrderStep(orderStep);
                for (Step step : stepsInCircuit) {
                    if (step.getOrderStep() >= orderStep) {
                        step.setOrderStep(step.getOrderStep() + 1);
                    }
                }
                stepsRepository.saveAll(stepsInCircuit);
            }
        }
    }
    public void setOrderStepForUpdatedStep(Step steps, List<Step> stepsInCircuit, Integer orderStep) {
        if (isValidOrderStep(orderStep, steps)) {
            for (Step step : stepsInCircuit) {
                if (step.getOrderStep() >= orderStep) {
                    step.setOrderStep(step.getOrderStep() + 1);
                }
            }
            stepsRepository.saveAll(stepsInCircuit);
            steps.setOrderStep(orderStep);
        }
    }
    public boolean isValidOrderStep(Integer orderStep, Step steps) {
        return orderStep != null && orderStep > 0 && !orderStep.equals(steps.getOrderStep());
    }
    @Override
    @Transactional
    public void delete(Integer stepId) {
        Step stepToDelete = getById(stepId);
        Circuit circuit = stepToDelete.getCircuit();
        List<Step> steps = stepsRepository.findByCircuit(circuit, Sort.by(ORDER_STEP));
        int indexToDelete = steps.indexOf(stepToDelete);
        if (indexToDelete == -1) {
            throw new ExceptionControllerAdvice.GeneralException("Step with ID " + stepId + " not found in the circuit");
        }
        stepToDelete.getLinks().forEach(linksLsRepository::delete);
        if (indexToDelete > 0 && indexToDelete < steps.size() - 1) {
            Step previousStep = steps.get(indexToDelete - 1);
            Step nextStep = steps.get(indexToDelete + 1);
            LinksLs previousLinksLs = previousStep.getLinks().iterator().next();
            LinksLs nextLinksLs = nextStep.getLinks().iterator().next();

            LinksLs newLinksLs = new LinksLs();
            newLinksLs.setStep(previousStep);
            newLinksLs.setLocationStart(previousLinksLs.getLocationStart());
            newLinksLs.setLocationEnd(nextLinksLs.getLocationStart());
            newLinksLs.setDuration(previousLinksLs.getDuration());
            newLinksLs.setDistance(previousLinksLs.getDistance());
            newLinksLs.setMode(previousLinksLs.getMode());
            LinksLsId newLinksLsId = new LinksLsId();
            newLinksLsId.setFkidStep(previousStep.getId());
            newLinksLsId.setFkidLocationStart(previousLinksLs.getLocationStart().getId());
            newLinksLsId.setFkidLocationEnd(nextLinksLs.getLocationStart().getId());
            newLinksLsId.setFkidMode(previousLinksLs.getMode().getId());
            newLinksLs.setId(newLinksLsId);
            linksLsRepository.delete(previousLinksLs);
            linksLsRepository.save(newLinksLs);
        }
        stepsRepository.delete(stepToDelete);
    }
    @Override
    @Transactional
    public Object get(Integer id) {
        return stepsMapper.apply(this.getById(id));
    }
    @Override
    @Transactional
    public Object getAll() {
        return stepsRepository.findAll().stream().map(stepsMapper).toList();
    }
    public Step getById(Integer id) {
        return stepsRepository.findById(id).orElseThrow(
                ()->new ExceptionControllerAdvice.ObjectNotFoundException("Steps not found")
        );
    }
    @Override
    @Transactional
    public List<Object> getStepByCircuit(Integer circuitReference) {
        Circuit circuit = circuitRepository.findById(circuitReference)
                .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("Circuit with ID " + circuitReference + " not found"));
        List<Step> steps = stepsRepository.findByCircuit(circuit, Sort.by(ORDER_STEP));
        return steps
                .stream()
                .map(stepsMapper)
                .toList();
    }
}
