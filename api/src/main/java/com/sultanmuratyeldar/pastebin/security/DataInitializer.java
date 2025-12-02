//package com.sultanmuratyeldar.pastebin.security;
//
//import com.sultanmuratyeldar.pastebin.entity.Role;
//import com.sultanmuratyeldar.pastebin.repository.RoleRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataInitializer {
//
//    private final RoleRepository roleRepository;
//
//    public DataInitializer(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    @PostConstruct
//    public void initRoles() {
//        if (!roleRepository.existsByName("ROLE_USER")) {
//            roleRepository.save(new Role(null, "ROLE_USER"));
//
//        }
//        if (!roleRepository.existsByName("ROLE_ADMIN")) {
//            roleRepository.save(new Role(null, "ROLE_ADMIN"));
//        }
//    }
//}
