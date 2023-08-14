package com.smart.controllers;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    //handler for search contact result
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Contact>> search(@PathVariable("name") String name,
                                                Principal principal) {

        //fetch user from database
        User user = this.userRepository.getUserByUserName(principal.getName());

        //fetch list of contact from database by keyword (custom finder method)
        List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(name, user);

        return ResponseEntity.ok(contacts);
    }
}