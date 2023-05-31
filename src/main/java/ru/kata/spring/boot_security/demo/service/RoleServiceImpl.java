package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;
import java.util.Optional;


@Service
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> allRole(){
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> foundRole = roleRepository.findById(id);
        if (foundRole.isPresent()) {
            return foundRole.get();
        } else {
            throw new UsernameNotFoundException(String.format("Role with id - %s not found", id));
        }
    }

}
