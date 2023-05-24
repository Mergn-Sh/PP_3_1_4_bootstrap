package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.*;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping()
    public String showAllUsers(Model model){
        List<User> allUsers = userService.allUsers();
        model.addAttribute("allUsers", allUsers);

        return "all-users";
    }

    @GetMapping("/addNewUser")
    public String addUser(Model model){
        User user = new User();
        model.addAttribute("newUser", user);
        Set<Role> roles = Set.of(roleService.saveRole(new Role("ROLE_ADMIN")), roleService.saveRole(new Role("ROLE_USER")));
        model.addAttribute("newSetRoles", roles);

        return "save-info";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("newUser") User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.saveUser(user);

        return "redirect:/admin";
    }

    @GetMapping("/updateInfo/{id}")
    public String updateInfo(@PathVariable("id") Long id, Model model){
        model.addAttribute("updateUser", userService.getUser(id));
        Set<Role> roles = userService.getUser(id).getRoles();
        Set<Role> rolesFull = Set.of(new Role("ROLE_ADMIN"), new Role("ROLE_USER"));
        Set<Role> roleUser = Set.of(new Role("ROLE_USER"));
        if(roles.isEmpty()){
            roles = Set.of(roleService.saveRole(new Role("ROLE_ADMIN")), roleService.saveRole(new Role("ROLE_USER")));
            model.addAttribute("updateRole", roles);
        } else if (roles.equals(rolesFull)) model.addAttribute("updateRole", roles);
        else if (roles.equals(roleUser)) {
            roles.add(roleService.saveRole(new Role("ROLE_ADMIN")));
            model.addAttribute("updateRole", roles);
        } else {
            roles.add(roleService.saveRole(new Role("ROLE_USER")));
            model.addAttribute("updateRole", roles);
        }

        return "update-info";
    }

    @PutMapping("/updateUser")
    public String updateUser(@ModelAttribute("user") User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.updateUser(user);

        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
