package com.smart.controllers;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class ForgetPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    // for generating otp
    Random random = new Random();

    // handler for forgot password form
    @GetMapping("/forgot")
    public String forgotPasswordPage(Model model) {

        model.addAttribute("title", "Forgot Password - Smart Contact Manager");

        return "forgot_password_page";
    }

    // handler for open verify otp page
    @GetMapping("/verify")
    public String openVerifyOtpPage(Model model) {

        model.addAttribute("title", "Verification - Smart Contact Manager");

        return "verify_otp_page";
    }

    // handler for open reset password form
    @GetMapping("/reset-password")
    public String forgotPasswordForm(Model model) {

        model.addAttribute("title", "Reset Password - Smart Contact Manager");

        return "reset_password_form";
    }

    // handler for send otp
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email, HttpSession session) {

        // fetch user from database
        User user = this.userRepository.getUserByUserName(email);

        // check the provided email present in database or not
        if (user == null) {

            session.setAttribute("msg",
                    new Message("User not exist, create new account...!!", "alert-danger"));

            return "redirect:/forgot";
        }

        int otp = random.nextInt(99999);
        // String otp1 = new DecimalFormat("00000").format(new Random().nextInt(99999));
        System.out.println(otp);

        // message properties
        String subject = "Verification Mail";
        String message = "<h2> Your password reset OTP for Smart Contact Manager is : " + otp + "</h2>";

        // send email to user's registered email
        boolean result = this.emailService.sendEmail(email, subject, message);

        // if email cannot be sent
        if (!result) {

            session.setAttribute("msg",
                    new Message("Something went wrong, try again..!!", "alert-danger"));

            return "redirect:/forgot";
        }

        // set otp to session for check later
        session.setAttribute("otp", otp);
        session.setAttribute("user", user);

        session.setAttribute("msg",
                new Message("OTP sent successfully on your registered email..!", "alert-success"));

        return "redirect:/verify";
    }

    // handler for verify otp
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {

        // get sent otp from session
        int sentOtp = (int) session.getAttribute("otp");

        // verify the entered otp and sent otp are same or not
        if (sentOtp != otp) {

            session.setAttribute("msg", new Message("You have entered wrong OTP..!", "alert-danger"));

            return "redirect:/verify";
        }

        session.setAttribute("msg", new Message("Verified...!", "alert-success"));

        return "redirect:/reset-password";
    }

    // handler for reset old password
    @PostMapping("/reset")
    public String resetPassword(@RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session) {

        // getting user from session
        User user = (User) session.getAttribute("user");

        // matching the password and confirm password
        if (!password.trim().equalsIgnoreCase(confirmPassword.trim())) {

            session.setAttribute("msg", new Message("Password not matched..!", "alert-danger"));

            return "redirect:/reset-password";
        }

        // update new password into database
        user.setPassword(this.passwordEncoder.encode(password));
        this.userRepository.save(user);

        session.setAttribute("msg", new Message("Password reset successfully..!", "alert-success"));

        return "redirect:/login";
    }
}
