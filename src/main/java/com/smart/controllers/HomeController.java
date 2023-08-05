package com.smart.controllers;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    //handler for home page
    @GetMapping("/")
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

    //handler for showing register page
    @GetMapping("/signup")
    public String signUp(Model m) {

        m.addAttribute("title", "Sign Up - Smart Contact Manager");
        m.addAttribute("user", new User());

        return "register";
    }

    //handler for saving user into database
    @PostMapping("/add-user")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam(value = "check", defaultValue = "false") boolean check, Model model, HttpSession session) {

        try {

            //validation for terms and conditions
            if (!check) {

                System.out.println("Please accept terms & conditions...!");

                //throwing custom exception
                throw new Exception("Please accept terms & conditions..!");
            }

            //setting value into user, that fields are not available in form
            user.setActive(true);
            user.setType("user_role");

            //save user into database
            User result = this.userRepository.save(user);

            System.out.println(user);

            //return user object & success message to view
            model.addAttribute("user", result);
            session.setAttribute("msg", new Message("Registered successfully..!!", "alert-success"));

            return "register";

        } catch (Exception e) {

            e.printStackTrace();

            //sending error
            session.setAttribute("msg", new Message("Something went wrong..!!", "alert-danger"));

            return "register";
        }
    }

}