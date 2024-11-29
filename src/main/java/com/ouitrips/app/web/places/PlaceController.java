package com.ouitrips.app.web.places;

import com.ouitrips.app.constants.Response;
import com.ouitrips.app.services.places.IPlaceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${REST_NAME}/places")
@AllArgsConstructor
public class PlaceController {
    private final IPlaceService placeService;

    @PostMapping("/make")
    public ResponseEntity<?> makePlace(
            @RequestParam(value = "reference", required = false) Integer id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "popularity", required = false) String popularity,
            //param entity Info Place
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "phone2", required = false) String phone2,
            @RequestParam(value = "website", required = false) String website,
            @RequestParam(value = "address_place_info", required = false) String addressPlaceInfo,
            @RequestParam(value = "content_place_info", required = false) String contentPlaceInfo,
            //param entity Duration Place
            @RequestParam(value = "duration", required = false) Long duration,
            //param entity Pricing Place
            @RequestParam(value = "price_min", required = false) Double priceMin,
            @RequestParam(value = "price_max", required = false) Double priceMax,
            @RequestParam(value = "price", required = false) Double price,
            //param entity OpeningHour Place
            @RequestParam(value = "opening_time", required = false) LocalTime openingTime,
            @RequestParam(value = "close_time", required = false) LocalTime closeTime,
            //param entity TimeZone Place
            @RequestParam(value = "time_zone_name", required = false) String timeZoneName,
            //Param entity City
            @RequestParam(value = "city_reference", required = false) Integer cityReference,
            //place type
            @RequestParam(value = "place_type_code", required = false) String placeTypeCode,
            //place tag
            @RequestParam(value = "place_tag", required = false) String placeTag
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("title", title);
        params.put("description", description);
        params.put("content", content);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("address", address);
        params.put("popularity", popularity);
        params.put("phone", phone);
        params.put("phone2", phone2);
        params.put("website", website);
        params.put("addressPlaceInfo", addressPlaceInfo);
        params.put("contentPlaceInfo", contentPlaceInfo);
        params.put("duration", duration);
        params.put("priceMin", priceMin);
        params.put("priceMax", priceMax);
        params.put("price", price);
        params.put("openingTime", openingTime);
        params.put("closeTime", closeTime);
        params.put("timeZoneName", timeZoneName);
        params.put("cityReference", cityReference);
        params.put("placeTypeCode", placeTypeCode);
        params.put("placeTag", placeTag);

        Object savedPlace = placeService.save(params);

        if (id != null) {
            return Response.updatedSuccessMessage();
        } else {
            return Response.responseData(savedPlace);
        }
    }
    @GetMapping("/get_all")
    public ResponseEntity<?> getAllPlaces() {
        return Response.responseData(placeService.getAll());
    }
    @GetMapping("/get")
    public ResponseEntity<?> getPlace(@RequestParam Integer id) {
        return Response.responseData(placeService.get(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePlace(@RequestParam Integer id) {
        placeService.delete(id);
        return Response.deletedSuccessMessage();
    }
}
