package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping()
    public String pageUser(Model model, Principal principal){
        User user = userServiceImpl.findByEmail(principal.getName());
        StringBuilder topLine = new StringBuilder(user.getEmail() + " with roles: ");
        for (Role role: user.getRoles()) {
            topLine.append(role.toString()).append(" ");
        }
        model.addAttribute("topLine", topLine);
        model.addAttribute("user", user);

        return "page-user";
    }
}
