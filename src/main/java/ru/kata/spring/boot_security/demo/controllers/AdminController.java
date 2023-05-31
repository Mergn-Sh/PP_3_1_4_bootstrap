package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.*;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping()
    public String showAllUsers(Model model, Principal principal){
        List<User> allUsers = userServiceImpl.allUsers();
        model.addAttribute("allUsers", allUsers);
        User currentUser = userServiceImpl.findByEmail(principal.getName());
        StringBuilder topLine = new StringBuilder(currentUser.getEmail() + " with roles: ");
        for (Role role: currentUser.getRoles()) {
            topLine.append(role.toString()).append(" ");
        }
        model.addAttribute("topLine", topLine.toString());
        model.addAttribute("currentUser", currentUser);
        User newUser = new User();
        model.addAttribute("newUser", newUser);
        List<Role> roles = roleServiceImpl.allRole();
        model.addAttribute("newSetRoles", roles);

        return "all-users";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("newUser") User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userServiceImpl.saveUser(user);

        return "redirect:/admin";
    }

    @PatchMapping("/updateUser/{id}")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam("roleIds") Set<Long> roleIds){
        if(!(userServiceImpl.getUser(user.getId()).getPassword().equals(user.getPassword()))){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userServiceImpl.setUserRoles(user, roleIds);
        userServiceImpl.updateUser(user);

        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userServiceImpl.deleteUser(id);

        return "redirect:/admin";
    }
}
