package org.example.restApi;

import org.example.Service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Api {

    @Autowired
    private userService userService;

    @GetMapping("/")
    public String userLogin(){
        userService.login();
        return "User login endpoint called Successfully";
    }

}
