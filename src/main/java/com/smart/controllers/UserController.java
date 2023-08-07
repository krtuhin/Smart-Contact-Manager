package com.smart.controllers;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    //add common data in all pages
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {

        //fetching username from spring security
        String userName = principal.getName();
        System.out.println(userName);

        //fetching user from database using username (email)
        User user = this.userRepository.getUserByUserName(userName);
        System.out.println(user);

        //sending data from controller to view
        model.addAttribute("user", user);
    }

    //handler for user dashboard
    @GetMapping("/index")
    public String dashboard(Model model) {

        //sending data from controller to view
        model.addAttribute("title", "Dashboard - Smart Contact Manager");

        return "normal/user_dashboard";
    }

    //handler for open add contact form
    @GetMapping("/add")
    public String openAddContactForm(Model model) {

        //sending data to view
        model.addAttribute("title", "Add Contact - Smart Contact Manager");
        model.addAttribute("contact", new Contact());

        return "normal/add_contact";
    }

    //handler method for save contact into database
    @PostMapping("/add-contact")
    public String addContact(@Valid
                             @ModelAttribute("contact") Contact contact,
                             BindingResult bindingResult,
                             @RequestParam("image") MultipartFile file, Model model) {

        try {
            //checking field errors
            if (bindingResult.hasErrors()) {
                System.out.println(bindingResult);
                throw new Exception("Error occurred in contact fields, Fill contact data carefully..!");
            }

            //set data into contact object
            contact.setPicture(file.getOriginalFilename());

            //saving contact into database
            Contact result = this.contactRepository.save(contact);
            System.out.println(contact);

            //sending data from controller to view
            model.addAttribute("contact", result);

        } catch (Exception e) {

            e.printStackTrace();

            return "normal/add_contact";
        }

        return "";
    }

}
