package com.heymart.balance.service;

import com.heymart.balance.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class UserServiceClientImpl implements UserServiceClient {
    RestTemplate restTemplate = new RestTemplate();
    String userServiceUrl = "http://localhost:8080/api/user";

    @Autowired
    public UserServiceClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyOwnerIdIsOwner(String token, String ownerId, String OwnerType) {
        if (token == null || ownerId == null) {
            return false;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            String url = userServiceUrl + "/get";
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserDTO.class
            );
            if (OwnerType.equals("supermarket")) {
                return ownerId.equals(Objects.requireNonNull(response.getBody()).getSupermarketId());
            }
            return ownerId.equals(Objects.requireNonNull(response.getBody()).getId());
        }
        catch(Exception e){
            return false;
        }
    }
}
