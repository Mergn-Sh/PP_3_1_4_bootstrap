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
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
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

//    @GetMapping("/addNewUser")
//    public String addUser(Model model){
//        User user = new User();
//        model.addAttribute("newUser", user);
//        List<Role> roles = roleServiceImpl.allRole();
//        model.addAttribute("newSetRoles", roles);
//
//        return "save-info";
//    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("newUser") User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userServiceImpl.saveUser(user);

        return "redirect:/admin";
    }

    @GetMapping("/updateInfo/{id}")
    public String updateInfo(@PathVariable("id") Long id, Model model){
        model.addAttribute("updateUser", userServiceImpl.getUser(id));
        model.addAttribute("updateRole", roleServiceImpl.allRole());

        return "update-info";
    }

    @PutMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user){
        if(!(userServiceImpl.getUser(user.getId()).getPassword().equals(user.getPassword()))){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userServiceImpl.updateUser(user);

        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userServiceImpl.deleteUser(id);

        return "redirect:/admin";
    }
}
