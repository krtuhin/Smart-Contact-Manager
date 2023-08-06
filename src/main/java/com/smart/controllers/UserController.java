package com.smart.controllers;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    //handler for user dashboard
    @GetMapping("/index")
    public String dashboard(Model model, Principal principal) {

        //fetching username from spring security
        String userName = principal.getName();
        System.out.println(userName);

        //fetching user from database using username (email)
        User user = this.userRepository.getUserByUserName(userName);
        System.out.println(user);

        //sending data from controller to view
        model.addAttribute("user", user);
        model.addAttribute("title", "Dashboard - Smart Contact Manager");

        return "normal/user_dashboard";
    }
}
