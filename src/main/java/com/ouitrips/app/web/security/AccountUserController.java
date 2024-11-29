package com.ouitrips.app.web.security;

import com.ouitrips.app.constants.Response;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.services.security.AccountUsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("${REST_NAME}/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AccountUserController {
    private  final AccountUsersService accountUsersService;

    @PostMapping("/add")
    ResponseEntity<?> addUser( @RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam(name="last_name" , required = false) String lastName,
                               @RequestParam(name="first_name",required = false)String firstName,
                               @RequestParam(name="birth_date",required = false) String birthDate,
                               @RequestParam(name="birth_place",required = false) String birthPlace,
                               @RequestParam(required = false) String phone,
                               @RequestParam(name="mobile_phone" , required = false) String mobilePhone,
                               @RequestParam(required = false) String gsm,
                               @RequestParam(name= "prefixe",required = false) String prefixe,
                               @RequestParam(required = false) String iso2,
                               @RequestParam(required = false) String profession,
                               @RequestParam(required = false) String civility,
                               @RequestParam(name = "avatar", required = false) String picture,
                               @RequestParam(name="company_name",required = false) String companyName,
                               @RequestParam(required = false) String address,
                               @RequestParam( name = "zip_code",required = false) String zipCode,
                               @RequestParam(name="additional_address",required = false) String additionalAddress,
                               @RequestParam(required = false) String region,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String country,
                               @RequestParam String roles){
        Map<String, Object> userAccountRequest = requestToMap(null, username, email, password, lastName, firstName, birthDate, birthPlace, phone,
                mobilePhone, gsm, prefixe, iso2, profession, civility, picture, companyName, address, zipCode,
                additionalAddress, region, city, country, roles);
        User user = accountUsersService.addUser(userAccountRequest);
        return Response.responseData(Map.of("reference", user.getReference()));
    }

    @PutMapping("/update")
    ResponseEntity<?> updateUser( @RequestParam("reference") String reference,
                                  @RequestParam String username,
                                  @RequestParam String email,
                                  @RequestParam(required = false) String password,
                                  @RequestParam(name="last_name" , required = false) String lastName,
                                  @RequestParam(name="first_name",required = false)String firstName,
                                  @RequestParam(name="birth_date",required = false) String birthDate,
                                  @RequestParam(name="birth_place",required = false) String birthPlace,
                                  @RequestParam(required = false) String phone,
                                  @RequestParam(name="mobile_phone" , required = false) String mobilePhone,
                                  @RequestParam(required = false) String gsm,
                                  @RequestParam(name="prefix",required = false) String prefixe,
                                  @RequestParam(required = false) String iso2,
                                  @RequestParam(required = false) String profession,
                                  @RequestParam(required = false) String civility,
                                  @RequestParam(name = "avatar", required = false) String picture,
                                  @RequestParam(name="company_name",required = false) String companyName,
                                  @RequestParam(required = false) String address,
                                  @RequestParam( name = "zip_code",required = false) String zipCode,
                                  @RequestParam(name="additional_address",required = false) String additionalAddress,
                                  @RequestParam(required = false) String region,
                                  @RequestParam(required = false) String city,
                                  @RequestParam(required = false) String country,
                                  @RequestParam String roles){
        Map<String, Object> userAccountRequest = requestToMap(reference, username, email, password, lastName, firstName, birthDate, birthPlace, phone,
                mobilePhone, gsm, prefixe, iso2, profession, civility, picture, companyName, address, zipCode,
                additionalAddress, region, city, country, roles);
        if(reference != null && username != null && email != null && password != null) {
            accountUsersService.updateUser(userAccountRequest);
        }
        return Response.updatedSuccessMessage();
    }
    @DeleteMapping("/delete")
    ResponseEntity<?> deleteUser(@RequestParam(value = "reference") String reference){
        accountUsersService.deleteUser(reference);
        return Response.deletedSuccessMessage();
    }
    @PostMapping("/get")
    ResponseEntity<?> getUser(@RequestParam String reference){
        return Response.responseData(accountUsersService.getUser(reference));
    }
    @GetMapping("/all")
    ResponseEntity<?> getAllUsers(){
        return Response.responseData(accountUsersService.getAllUsers());
    }

    private Map<String, Object> requestToMap(
            String reference, String username, String email, String password, String lastName, String firstName, String birthDate, String birthPlace, String phone,
            String mobilePhone, String gsm, String prefixe, String iso2, String profession, String civility,String  picture, String companyName, String address, String zipCode,
            String additionalAddress, String region, String city, String country, String roles
    ){
        Map<String, Object> userAccountRequest = new HashMap<>();
        userAccountRequest.put("userReference",reference);
        userAccountRequest.put("username", username);
        userAccountRequest.put("email", email);
        userAccountRequest.put("password", password);
        userAccountRequest.put("lastName", lastName);
        userAccountRequest.put("firstName", firstName);
        userAccountRequest.put("birthDate", birthDate);
        userAccountRequest.put("birthPlace", birthPlace);
        userAccountRequest.put("phone", phone);
        userAccountRequest.put("mobilePhone", mobilePhone);
        userAccountRequest.put("gsm", gsm);
        userAccountRequest.put("prefixe", prefixe);
        userAccountRequest.put("iso2", iso2);
        userAccountRequest.put("profession", profession);
        userAccountRequest.put("civility", civility);
        userAccountRequest.put("picture", picture);
        userAccountRequest.put("companyName", companyName);
        userAccountRequest.put("address", address);
        userAccountRequest.put("zipCode", zipCode);
        userAccountRequest.put("additionalAddress", additionalAddress);
        userAccountRequest.put("region", region);
        userAccountRequest.put("city", city);
        userAccountRequest.put("country", country);
        userAccountRequest.put("roles", roles);
        return userAccountRequest;
    }
}
