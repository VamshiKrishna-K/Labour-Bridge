package com.lb.controllers;

import com.lb.Services.SqlUserService;
import com.lb.entities.Users;
import com.lb.entities.WorkRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ConstructorController {

    @Autowired
    private SqlUserService userService;

    @GetMapping("/constructor_dashboard")
    public String constructorDashboard(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        
        return "constructor/constructor_dashboard";
    }
   
   @GetMapping("/constructorMain")
public String constructorMainPage(Model model, HttpSession session) {
    // Get logged-in user from session
    Users sessionUser = (Users) session.getAttribute("loggedInUser");

    if (sessionUser == null) {
      
        return "redirect:/login";
    }

    // Refresh user info from DB to ensure latest status
    Users freshUser = userService.getUserById(sessionUser.getId());
    session.setAttribute("loggedInUser", freshUser); 
    model.addAttribute("user", freshUser);

    
    int pendingWorks = userService.getCountOfPendingWorks(freshUser.getId());
    model.addAttribute("pendingworks", pendingWorks);

 

    
    if (freshUser.isEnabled()) {
        return "constructor/constructorMain";
    }

    return "redirect:/notApproved";
}

        
   

    @GetMapping("/constructor_post_work")
    public String postWorkPage(Model model,HttpSession session) {
        model.addAttribute("workRequest", new WorkRequest());
        Users user = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("workRequests", userService.getWorkRequestsByConstructorId(user.getId()));
        return "constructor/constructor_post_work";
    }
     @GetMapping("/constructor_my_requests")
    public String WorkRequest(Model model,HttpSession session) {
        model.addAttribute("workRequest", new WorkRequest());
        return "constructor/constructor_my_requests";
    }

    @PostMapping("/send_request")
    public String submitWorkRequest(@ModelAttribute @Valid WorkRequest workRequest,
                                    BindingResult result,
                                    HttpSession session) {

        Users user = (Users) session.getAttribute("loggedInUser");
        if (user == null) {
        // redirect to login or show error
        return "redirect:/login?error=sessionExpired";
    }
        if (result.hasErrors()) {
            return "constructor/constructor_post_work";
        }

        userService.saveWorkRequest(
                user.getId(),
                workRequest.getRegion(),
                workRequest.getNumLabourers(),
                workRequest.getStartDate(),
                "Pending"
        );

        return "redirect:/constructor_post_work";
    }

    @GetMapping("/constructor_profile")
    public String constructorProfile(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        return "constructor/constructor_profile";
    }

}