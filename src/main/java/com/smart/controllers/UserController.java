package com.smart.controllers;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.FileUploadHelper;
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
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {

        //sending data to view
        model.addAttribute("title", "Add Contact - Smart Contact Manager");
        model.addAttribute("contact", new Contact());

        return "normal/add_contact";
    }

    //handler method for save contact into database
    @PostMapping("/save-contact")
    public String addContact(@Valid
                             @ModelAttribute("contact") Contact contact,
                             BindingResult bindingResult,
                             @RequestParam("image") MultipartFile file,
                             Model model, Principal principal) {

        try {
            //checking field errors
            if (bindingResult.hasErrors()) {
                System.out.println(bindingResult);
                throw new Exception("Error occurred in contact fields, Fill contact data carefully..!");
            }

            //saving file into server
            if (file.isEmpty()) {

                //set data into contact object
                contact.setPicture("default.png");

            } else if (!file.getContentType().trim().contains("image")) {

                throw new Exception("Only image file can be uploaded..!!");

            } else {

                contact.setPicture(file.getOriginalFilename());

                boolean f = FileUploadHelper.uploadFile(file);

                if (!f) {

                    throw new Exception("File not uploaded..!!");
                }
            }

            //contact saving process
            //fetching username
            String userName = principal.getName();

            //fetch user from database
            User user = this.userRepository.getUserByUserName(userName);

            //data add bidirectional mapping way
            contact.setUser(user);
            user.getContacts().add(contact);

            //save user into database after modify data
            User result = this.userRepository.save(user);
            System.out.println(result);

            //saving contact into database
            //Contact result = this.contactRepository.save(contact);
            //System.out.println(contact);

            //sending data from controller to view
            model.addAttribute("contact", contact);

        } catch (Exception e) {

            e.printStackTrace();

            return "normal/add_contact";
        }

        return "";
    }

}
