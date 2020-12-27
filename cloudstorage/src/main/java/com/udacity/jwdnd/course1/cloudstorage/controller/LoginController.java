package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping()
    public String loginView() {
        return "login";
    }

    @PostMapping()
    public String signupUser(Model model){
        String logoutError = null;
        if (logoutError == null) {
            model.addAttribute("param.logout", true);
        } else {
            logoutError = "Error logging you out!";
            model.addAttribute("param.error",logoutError);
        }

        return "login";
    }
}
