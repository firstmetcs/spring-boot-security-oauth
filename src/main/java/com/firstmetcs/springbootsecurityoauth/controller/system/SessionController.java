package com.firstmetcs.springbootsecurityoauth.controller.system;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {

    @GetMapping("")
    public Object getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}