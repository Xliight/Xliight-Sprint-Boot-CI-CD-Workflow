package com.ouitrips.app.services.geolocation;
import com.ouitrips.app.dtos.geolocation.ResponseAPIDTO;
import com.ouitrips.app.entities.geolocation.ResponseAPI;

import java.util.List;
import java.util.Optional;

public interface IFetchDataService {
    List<ResponseAPI> getAllResponses();

    Optional<ResponseAPI> getResponseById(Integer id);

    ResponseAPI createResponse(ResponseAPI responseAPI);

    // Optional methods for updating and deleting if needed
    ResponseAPI updateResponse(Integer id, ResponseAPI responseAPI);

    void deleteResponse(Integer id);

    ResponseAPIDTO convertToDTO(ResponseAPI responseAPI);
    List<ResponseAPIDTO> getAllResponseDTOs();

}
