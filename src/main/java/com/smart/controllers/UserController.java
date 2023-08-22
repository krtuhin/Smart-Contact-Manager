package com.smart.controllers;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.smart.dao.ContactRepository;
import com.smart.dao.PaymentRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.Payments;
import com.smart.entities.User;
import com.smart.helper.FileUploadHelper;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private PaymentRepository paymentRepository;

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
                             Model model, Principal principal, HttpSession session) {

        try {
            //checking field errors
            if (bindingResult.hasErrors()) {
                System.out.println(bindingResult);

                return "normal/add_contact";
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

            //session attribute
            session.setAttribute("msg", new Message("Contact added successfully..!!", "alert-success"));

        } catch (Exception e) {

            e.printStackTrace();

            String content = "Something went wrong..!!";

            if (e.toString().contains("Only image")) {

                content = "Only image file can be uploaded..!!";
            }

            //session attribute
            session.setAttribute("msg", new Message(content, "alert-danger"));

            return "normal/add_contact";
        }

        return "redirect:/user/view-contacts/0";
    }

    //handler for show contacts page
    //getting data as page
    @GetMapping("/view-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page,
                               Model model, Principal principal) {

        try {

            //fetch current user from database using userName
            String userName = principal.getName();
            User user = this.userRepository.getUserByUserName(userName);

            //create Pageable object
            // page - current page number
            // 5 - datalist per page
            Pageable pageable = PageRequest.of(page, 5);

            //fetch contacts of one page from database using userid
            Page<Contact> list = this.contactRepository.findContactsByUserId(user.getId(), pageable);

            System.out.println(list);

            //sending data to view
            model.addAttribute("title", "View Contacts - Smart Contact Manager");
            model.addAttribute("contacts", list);

            //sending page information to view
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", list.getTotalPages());

        } catch (Exception e) {

            e.printStackTrace();

        }
        return "normal/show_contacts";
    }

    //handler for contact details page
    @GetMapping("/contact/{id}")
    public String contactDetails(@PathVariable("id") int cId,
                                 Model model, Principal principal) {

        //fetching single contact from database
        Optional<Contact> contactList = this.contactRepository.findById(cId);
        Contact contact = contactList.get();

        //getting current userName
        String userName = principal.getName();

        //checking the particular contact is this user's or not
        if (!contact.getUser().getEmail().equals(userName)) {

            return "redirect:/user/view-contacts/0";
        }

        //sending data to view
        model.addAttribute("title", "Contact Details - Smart Contact Manager");
        model.addAttribute("contact", contact);

        return "normal/contact_details";
    }

    //handler for delete single contact
    @GetMapping("/delete-contact/{id}")
    public String deleteContact(@PathVariable("id") int cId, Model model,
                                Principal principal, HttpSession httpSession) {

        //message object
        Message message = new Message();

        try {

            //fetching user from database by email
            String userName = principal.getName();
            User user = this.userRepository.getUserByUserName(userName);

            //getting single contact from database using contact id
            Optional<Contact> optionalContact = this.contactRepository.findById(cId);
            Contact contact = optionalContact.get();

            //checking owner of contact
            if (user.getId() == contact.getUser().getId()) {

                //delete contact
                this.contactRepository.deleteById(cId);

                //setting value of success message
                message.setContent("Deleted Successfully...!!");
                message.setType("alert-success");

                //sending success message
                httpSession.setAttribute("msg", message);

            } else {

                //throwing exception for not matching user
                throw new Exception("You do not have any contact with this id..!!");
            }
        } catch (Exception e) {

            e.printStackTrace();

            //setting value of error message
            message.setContent("Permission Denied...!!");
            message.setType("alert-danger");

            //sending error message
            httpSession.setAttribute("msg", message);
        }
        return "redirect:/user/view-contacts/0";
    }

    //handler for open update contact form
    @GetMapping("/update-contact/{id}")
    public String updateContact(@PathVariable("id") int cId,
                                Model model, Principal principal) {

        try {

            //fetching user from database
            User user = this.userRepository.getUserByUserName(principal.getName());

            //fetching contact from database by id
            Contact contact = this.contactRepository.findById(cId).get();

            if (user.getId() == contact.getUser().getId()) {

                model.addAttribute("contact", contact);

            } else {
                throw new Exception("You do not have any contact with this id..!!");
            }


        } catch (Exception e) {

            e.printStackTrace();

            return "redirect:/user/view-contacts/0";
        }
        //sending data to view
        model.addAttribute("title", "Update Contact - Smart Contact Manager");

        return "normal/update_contact";
    }

    //handler for update contact
    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("contact") Contact contact,
                         BindingResult bindingResult,
                         @RequestParam("image") MultipartFile file,
                         Principal principal, HttpSession session) {

        //success or error message object
        Message message = new Message();

        try {
            //if any error occurred in input fields
            if (bindingResult.hasErrors()) {

                throw new Exception("Mandatory fields cannot be empty..!!");
            }

            //fetch username
            String userName = principal.getName();

            //fetching user from database by email
            User user = this.userRepository.getUserByUserName(userName);

            //old contact details
            Contact oldContact = this.contactRepository.findById(contact.getId()).get();

            //set user of contact
            contact.setUser(user);

            //image file validation
            if (file.isEmpty()) {

                //if image empty
                contact.setPicture(oldContact.getPicture());

            } else if (!file.getContentType().contains("image")) {

                //if input file is not an image
                throw new Exception("Only image can be uploaded..!");

            } else {

                //delete old image from server
                FileUploadHelper.deleteFile(oldContact.getPicture());

                //saving image into server after validate
                boolean isSave = FileUploadHelper.uploadFile(file);

                //update contact image name in database
                contact.setPicture(file.getOriginalFilename());

                //if image save failed
                if (!isSave) {
                    throw new Exception("File not uploaded..!!");
                }
            }

            //saving contact into database
            Contact result = this.contactRepository.save(contact);

            //sending success message
            message.setContent("Contact updated successfully..!!");
            message.setType("alert-success");
            session.setAttribute("msg", message);

            return "redirect:/user/view-contacts/0";

        } catch (Exception e) {

            e.printStackTrace();

            //error message content
            String content = "Something went wrong, try again..!!";

            //error message based on condition
            if (e.toString().contains("image")) {

                content = "Select valid image..!!";

            } else if (e.toString().contains("fields")) {

                content = "Mandatory fields cannot be empty..!!";
            }

            //sending error message
            message.setType("alert-danger");
            message.setContent(content);
            session.setAttribute("msg", message);

            return "redirect:/user/update-contact/" + contact.getId();
        }
    }

    //handler to show user profile page
    @GetMapping("/profile")
    public String userProfile(Model model) {

        model.addAttribute("title", "Your Profile - Smart Contact Manager");

        return "normal/user_profile";
    }

    //handler for open settings
    @GetMapping("/settings")
    public String openSetting(Model model) {

        //sending data from controller to view
        model.addAttribute("title", "Settings - Smart Contact Manager");

        return "normal/settings_page";
    }

    //handler for processing change password form
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Principal principal, HttpSession session) {

        //fetch current user using username
        User user = this.userRepository.getUserByUserName(principal.getName());

        //matching old password
        if (this.bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {

            //setting encoded new password into user object
            user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));

            //update new password into database
            this.userRepository.save(user);

            //sending success message using session
            session.setAttribute("msg",
                    new Message("Password changed successfully..!", "alert-success"));

        } else {

            //sending error message using session
            session.setAttribute("msg",
                    new Message("Old password wrong..!", "alert-warning"));

            return "redirect:/user/settings";
        }

        return "redirect:/user/index";
    }

    //handler for creating payment order
    @PostMapping("/create-order")
    @ResponseBody
    public String paymentOrder(@RequestBody Map<String, Object> data, Principal principal) {

        //getting amount
        int amount = Integer.parseInt(data.get("amount").toString());

        try {

            //RazorPay client object
            RazorpayClient client = new RazorpayClient("rzp_test_JZAipOiOr7CDus", "LyBNwqYxfkpIsfhv0On57ewd");

            //json object
            JSONObject json = new JSONObject();

            json.put("amount", amount * 100);
            json.put("currency", "INR");
            json.put("receipt", "txn_345");

            //creating order
            Order order = client.orders.create(json);

            //current user using user email
            User user = this.userRepository.getUserByUserName(principal.getName());

            //payment object
            Payments payments = new Payments();

            //setting data into payment object
            payments.setOrderId(order.get("id"));
            payments.setAmount(order.get("amount"));
            payments.setStatus("created");
            payments.setUser(user);
            payments.setReceipt(order.get("receipt"));

            //saving payment data into database
            this.paymentRepository.save(payments);

            return order.toString();

        } catch (RazorpayException e) {

            e.printStackTrace();
        }

        return null;
    }

    //method for update payment on serer
    @PostMapping("/update-payment")
    @ResponseBody
    public ResponseEntity<?> updatePayment(@RequestBody Map<String, Object> data) {

        //getting payment details from database by order id
        Payments payments = this.paymentRepository.findByOrderId(data.get("order_id").toString());

        //updating payment status and payment id
        payments.setStatus(data.get("status").toString());
        payments.setPaymentId(data.get("payment_id").toString());

        //update into database
        this.paymentRepository.save(payments);

        return ResponseEntity.ok(Map.of("message", "updated"));
    }
}
