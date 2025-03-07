package com.example.MobileStore.service;

import com.example.MobileStore.dto.RegisterDTO;
import com.example.MobileStore.entity.Role;
import com.example.MobileStore.entity.User;
import com.example.MobileStore.exception.AlreadyExistsException;
import com.example.MobileStore.exception.NotFoundException;
import com.example.MobileStore.mapper.UserMapper;
import com.example.MobileStore.repository.RoleRepository;
import com.example.MobileStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterDTO saveUser(RegisterDTO registerDTO){
        if (userRepository.existsByUsername(registerDTO.getUsername())){
            throw new AlreadyExistsException("User","userName",registerDTO.getUsername());
        }
        Role role=roleRepository.findById(registerDTO.getRole()).orElseThrow(()->new NotFoundException(registerDTO.getRole()));

        //convert UserDTO to User
        User user=userMapper.toEntity(registerDTO);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(role);

        return userMapper.toDTO(userRepository.save(user));
    }

    public Optional<User>getUserByUsername(String userName){
        return userRepository.findByUsername(userName);
    }
}
