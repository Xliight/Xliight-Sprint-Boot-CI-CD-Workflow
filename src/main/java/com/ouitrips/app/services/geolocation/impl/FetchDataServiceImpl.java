package com.ouitrips.app.services.geolocation.impl;

import com.ouitrips.app.dtos.geolocation.ResponseAPIDTO;
import com.ouitrips.app.entities.geolocation.ResponseAPI;

import com.ouitrips.app.repositories.security.geolocation.ResponseAPIRepository;
import com.ouitrips.app.services.geolocation.IFetchDataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FetchDataServiceImpl implements IFetchDataService {

    private final ResponseAPIRepository responseAPIRepository;

    @Override
    public List<ResponseAPI> getAllResponses() {
        return responseAPIRepository.findAll();
    }

    @Override
    public Optional<ResponseAPI> getResponseById(Integer id) {
        return responseAPIRepository.findById(id);
    }

    @Override
    public ResponseAPI createResponse(ResponseAPI responseAPI) {
        return responseAPIRepository.save(responseAPI);
    }

    @Override
    public ResponseAPI updateResponse(Integer id, ResponseAPI responseAPI) {
        if (!responseAPIRepository.existsById(id)) {
            return null; // or throw an exception
        }
        responseAPI.setId(id);
        return responseAPIRepository.save(responseAPI);
    }



    @Override
    public void deleteResponse(Integer id) {
        responseAPIRepository.deleteById(id);
    }

    /**
     * Converts a ResponseAPI entity to a ResponseAPIDTO.
     *
     * @param responseAPI the ResponseAPI entity
     * @return the corresponding ResponseAPIDTO
     */
    public ResponseAPIDTO convertToDTO(ResponseAPI responseAPI) {
        ResponseAPIDTO dto = new ResponseAPIDTO();
        dto.setId(responseAPI.getId());
        dto.setJsonResponse(responseAPI.getJsonResponse());
        if (responseAPI.getRequestAPI() != null) {
            dto.setRequestAPIId(responseAPI.getRequestAPI().getId());
        }
        return dto;
    }

    /**
     * Retrieves all ResponseAPIDTOs.
     *
     * @return a list of ResponseAPIDTOs
     */
    public List<ResponseAPIDTO> getAllResponseDTOs() {
        return responseAPIRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }




}