package com.telusko1.SpringSecEx.controller;

import com.telusko1.SpringSecEx.model.Users;
import com.telusko1.SpringSecEx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService service;
    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return service.register(user);
    }


    @PostMapping("/logins")
    public String login(@RequestBody Users user) {


        return service.verify(user);
    }



}
