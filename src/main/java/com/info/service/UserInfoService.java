package com.info.service;


import com.info.entity.UserInfo;
import com.info.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    private final UserInfoRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Optional<UserInfo> optionalUserInfo =  repository.findByEmail(username);
        if (optionalUserInfo.isPresent()) {
            UserInfo userInfo = optionalUserInfo.get();
            return new UserInfoDetails(UserInfo.builder()
                    .email(userInfo.getEmail())
                    .password(encoder.encode(userInfo.getPassword())) // Use a default password or fetch from DB
                    .roles(userInfo.getRoles()) // Default role, can be modified based on your logic
                    .build());
        }

       return new UserInfoDetails(UserInfo.builder()
                .email(username)
                .password(encoder.encode("defaultPassword")) // Use a default password or fetch from DB
                .roles("ROLE_USER") // Default role, can be modified based on your logic
                .build());

    }

    public String addUser(UserInfo userInfo) {
        repository.save(userInfo);
        // your logic to add user
        return "User added successfully";
    }
}