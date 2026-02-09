package com.lb.controllers;

import com.lb.Services.SqlUserService;
import com.lb.entities.Users;
import com.lb.forms.LoginForm;
import com.lb.forms.UserForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    @Autowired
    private SqlUserService userService;

    // ---------- Common Pages ----------
    @GetMapping("/")
    public String homePage() {
        return "common/home";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "common/about";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "common/contact";
    }

    // ---------- Login / Register ----------
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginform", new LoginForm());
        return "common/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "common/register";
    }

    @PostMapping("/do-login")
    public String loginProcess(@ModelAttribute("loginform") LoginForm loginform,
                               HttpSession session, Model model) {

        if (!userService.verifyLogin(loginform.getEmail(), loginform.getPassword())) {
            session.setAttribute("message", "Invalid credentials. Try again!");
            return "redirect:/login";
        }

        Users user = userService.getUserByEmail(loginform.getEmail());
        session.setAttribute("loggedInUser", user);

        switch (user.getRole()) {
            case Mediator:
                return "redirect:/mediatorMain";
            case Constructor:
                return "redirect:/constructorMain";
            case Admin:
                return "redirect:/admin_dashboard";
            default:
                return "redirect:/login";
        }
    }
    
    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("userForm") UserForm userForm,
                                  BindingResult bindResult,
                                  HttpSession session,
                                  Model model) {

        if (bindResult.hasErrors() ||
                !userForm.getPassword().equals(userForm.getRepeatPassword())) {

            if (!userForm.getPassword().equals(userForm.getRepeatPassword())) {
                model.addAttribute("passwordError", "Passwords do not match");
            }

            return "common/register";
        }

        userService.saveUser(
                userForm.getName(),
                userForm.getEmail(),
                userForm.getPassword(),
                userForm.getPhone(),
                userForm.getRole());
        
        session.setAttribute("message", "Registration successful! Please login.");
        return "redirect:/login";
    }
    @GetMapping("/notApproved")
    public String mediatorNoTApproved(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
      
        return "common/notApproved";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
