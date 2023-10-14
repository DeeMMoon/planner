package com.example.planner.utils.rest.resttemplate;

import com.example.planner.entity.entity.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class UserRestBuilder {

    private static final String baseUrl = "http://localhost:8765/planner-users/user/";

    public boolean userExists(Long userId){

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Long> request = new HttpEntity(userId);

        ResponseEntity<User> response = null;

        try {

            response = restTemplate.exchange(baseUrl+"/id", HttpMethod.POST, request, User.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
