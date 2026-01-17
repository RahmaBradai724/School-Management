package com.example.SpringProject.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalDataAdvice {

    @ModelAttribute("requestURI")
    public String requestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
