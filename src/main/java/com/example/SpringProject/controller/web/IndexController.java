package com.example.SpringProject.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else if (roles.contains("ROLE_STUDENT")) {
            return "redirect:/student/dashboard";
        } else if (roles.contains("ROLE_TEACHER")) {
            return "redirect:/teacher/dashboard";
        }

        return "redirect:/login";
    }
}
