package com.lb.controllers;

import com.lb.Services.SqlLabourService;
import com.lb.Services.SqlUserService;
import com.lb.entities.Labourer;
import com.lb.entities.Users;
import com.lb.forms.LabourForm;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MediatorController {

    @Autowired
    private SqlLabourService sqlLabourService;

    @Autowired
    private SqlUserService userService;

    @GetMapping("/mediator_dashboard")
    public String mediatorDashboard(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        int totalLabourers = sqlLabourService.countLabourersByMediator(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("labour_count", totalLabourers);

        return "mediator/mediator_dashboard";
    }
    

    
  @GetMapping("/mediatorMain")
public String mediatorMainPage(Model model, HttpSession session) {
    // Get the logged-in user from session
    Users sessionUser = (Users) session.getAttribute("loggedInUser");

    if (sessionUser == null) {
     
        return "redirect:/login";
    }

    //  Refresh user info from the database to get the latest enabled status
    Users freshUser = userService.getUserById(sessionUser.getId());

   
    session.setAttribute("loggedInUser", freshUser);

   
    model.addAttribute("user", freshUser);

    System.out.println("MediatorMain: enabled = " + freshUser.isEnabled());

    if (freshUser.isEnabled()) {
        return "mediator/mediatorMain";
    }

   
    return "redirect:/notApproved";
}

    @GetMapping("/mediator_register_labourer")
    public String registerLabourerPage(Model model) {
        model.addAttribute("labourForm", new LabourForm());
        return "mediator/mediator_register_labourer";
    }

    @PostMapping("/do_labour_register")
    public String processLabourRegister(@Valid @ModelAttribute LabourForm labourForm,
                                        BindingResult bindingResult,
                                        HttpSession session,
                                        Model model) {

        Users user = (Users) session.getAttribute("loggedInUser");

        if (bindingResult.hasErrors()) {
            return "mediator/mediator_register_labourer";
        }

        sqlLabourService.saveLabour(
                labourForm.getName(),
                labourForm.getAge(),
                labourForm.getPhone(),
                labourForm.getDailyWage(),
                user.getId());

        session.setAttribute("message", "Labourer registration successful!");
        return "redirect:/mediator_register_labourer";
    }

    @GetMapping("/mediator_labourer_list")
    public String mediatorLabourerList(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        List<Labourer> labourers = sqlLabourService.getLabourersByMediator(user.getId());
        model.addAttribute("labourers", labourers);
        return "mediator/mediator_labourer_list";
    }

    @GetMapping("/mediator_profile")
    public String mediatorProfile(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("loggedInUser");
        model.addAttribute("user", user);
        return "mediator/mediator_profile";
    }
    @GetMapping("/mediator_view_requests")
        public String  mediatorViewRequests(Model model,HttpSession session){
      
        model.addAttribute("workRequests",userService.getAllPendingRequests());
        return 
        "mediator/mediator_view_requests";
    }

    @GetMapping("/accept/{id}")
    public String acceptWorkRequest(@PathVariable("id") Long requestId, HttpSession session) {
        Users mediator = (Users) session.getAttribute("loggedInUser");

        if (mediator == null) {
            return "redirect:/login";
        }

        userService.acceptWorkRequest(requestId, mediator.getId());

        return "redirect:/mediator_view_requests";
    }
}
