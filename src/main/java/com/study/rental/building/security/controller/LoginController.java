package com.study.rental.building.security.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class LoginController {

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping("/home")
    public String home(Principal principal, Model model) {
        model.addAttribute("user", principal.getName());
        model.addAttribute("roles", ((UsernamePasswordAuthenticationToken) principal).getAuthorities());

        return "home";
    }

    @RequestMapping("/admin")
    public String admin(Principal principal, Model model){
        model.addAttribute("super", principal.getName());
        model.addAttribute("roles", ((UsernamePasswordAuthenticationToken) principal).getAuthorities());

        return "admin";
    }

    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }
}
