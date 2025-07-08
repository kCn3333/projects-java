package com.kcn.jobportal.controller;


import com.kcn.jobportal.entity.Users;
import com.kcn.jobportal.entity.UsersType;
import com.kcn.jobportal.service.UsersService;
import com.kcn.jobportal.service.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UsersController {

    private final UsersTypeService usersTypeService;
    private final UsersService usersService;

    public UsersController(UsersTypeService usersTypeService, UsersService usersService) {
        this.usersTypeService = usersTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model){
        List<UsersType> usersTypes=usersTypeService.getAll();
        Users user = new Users();
        user.setUserTypeId(usersTypes.get(1));
        model.addAttribute("getAllTypes", usersTypes);
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid Users users, Model model){
        System.out.println("Users:" + users);
        if(usersService.getUserByEmail(users.getEmail()).isEmpty()) {
            usersService.addNewUser(users);

            return "redirect:/dashboard/";
        } else {
            model.addAttribute("error", "Email already registered");
            List<UsersType> usersTypes = usersTypeService.getAll();
            model.addAttribute("getAllTypes", usersTypes);
            model.addAttribute("user", new Users());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null)
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        return "redirect:/";
    }




}
