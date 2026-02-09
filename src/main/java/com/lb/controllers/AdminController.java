package com.lb.controllers;

import com.lb.Services.SqlLabourService;
import com.lb.Services.SqlUserService;
import com.lb.entities.Labourer;
import com.lb.entities.Mediator;
import com.lb.entities.Users;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
public class AdminController {

    @Autowired
    private SqlUserService userService;
    @Autowired
    private SqlLabourService labourService;

    // ---------- Admin Dashboard ----------
    @GetMapping("/admin_dashboard")
    public String adminDashboard(Model model, HttpSession session) {
         Users user = (Users) session.getAttribute("loggedInUser");
    model.addAttribute("user", user);
    model.addAttribute("mediatorCount", userService.count("Mediator"));
    model.addAttribute("constructorCount", userService.count("Constructor"));
    model.addAttribute("totalLabours",labourService.count());
    return "admin/admin_dashboard";
        
    }

    // ---------- View All Mediators ----------
    @GetMapping("/admin_view_mediators")
    public String viewMediators(Model model, HttpSession session) {
        Users admin = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("user", admin);

        List<Users> mediators = userService.getUsersByRole("Mediator"); 
         model.addAttribute("mediators", mediators);
 
        return "admin/admin_view_mediators";
    }
    @GetMapping("/admin_view_labours")
    public String viewLabours(Model model, HttpSession session) {
        Users admin = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("user", admin);

        List<Labourer> labourers = labourService.getAllLabourers();
         model.addAttribute("labourers",labourers);
 
        return "admin/admin_view_labours";
    }
    // ---------- View All Constructors ----------
    @GetMapping("/admin_view_constructors")
    public String viewConstructors(Model model, HttpSession session) {
        Users admin = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("user", admin);

         List<Users> constructors = userService.getUsersByRole("Constructor");
        model.addAttribute("constructors", constructors); 

        return "admin/admin_view_constructors";
    }

    // ---------- Admin Profile ----------
    @GetMapping("/admin_profile")
    public String adminProfile(Model model, HttpSession session) {
        Users admin = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("user", admin);
        return "admin/admin_profile";
    }
  
 @GetMapping("/approval/{id}")
public String handleApprovalProcess(@PathVariable Long id,HttpSession session) {
   
    
    userService.handleApproval(id);
    Users user1=userService.getUserById(id);
    
    session.setAttribute("loggedInUser", user1); 
    
    
    String role = userService.getRoleById(id);
    switch (role) {
        case "Mediator":
            return "redirect:/admin_view_mediators"; 
        case "Constructor":
            return "redirect:/admin_view_constructors"; 
        default:
            return "redirect:/admin_dashboard"; 
    }
}

    

}
