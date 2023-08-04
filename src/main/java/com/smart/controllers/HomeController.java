package com.smart.controllers;

import com.smart.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    //handler for home page
    @GetMapping("/home")
    public String home(Model m) {

        m.addAttribute("title", "Home - Smart Contact Manager");

        return "home";

    }

    //handler for about page
    @GetMapping("/about")
    public String about(Model m) {

        m.addAttribute("title", "About - Smart Contact Manager");

        return "about";
    }

}
