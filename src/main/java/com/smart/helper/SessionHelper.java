package com.smart.helper;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {

    //method for remove message attribute from session
    public static void removeAttribute(String attribute) {

        try {
            //getting session from request context holder
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();

            //remove attribute
            session.removeAttribute(attribute);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}