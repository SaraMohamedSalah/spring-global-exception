package com.demo.HandlingException.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/admin"})
@PreAuthorize("hasRole('ADMIN')")
public class AdminRestController {
    public AdminRestController() {
    }

    @GetMapping({"/hello"})
    public String helloAdmin() {
        return "Hello Admin Authenticated";
    }
}
