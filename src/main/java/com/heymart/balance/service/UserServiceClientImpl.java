package com.heymart.balance.service;

import org.springframework.beans.factory.annotation.Value;
import com.heymart.balance.dto.UserDTO;
import com.heymart.balance.exceptions.BalanceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class UserServiceClientImpl implements UserServiceClient {
    RestTemplate restTemplate = new RestTemplate();
    @Value("${user.api}")
    String userServiceUrl;

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
            if (OwnerType.equals("Supermarket")) {
                return ownerId.equals(Objects.requireNonNull(response.getBody()).getSupermarketId());
            }
            boolean result1 = ownerId.equals(Objects.requireNonNull(response.getBody()).getId());
            boolean result2 = ownerId.equals(Objects.requireNonNull(response.getBody()).getSupermarketId());
            return result1 || result2;
        }
        catch(Exception e){
            throw new BalanceNotFoundException("bnf");
        }
    }
}
