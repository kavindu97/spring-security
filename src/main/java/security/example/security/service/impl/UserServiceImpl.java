package security.example.security.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.example.security.model.Role;
import security.example.security.model.User;
import security.example.security.repository.RoleRepository;
import security.example.security.repository.UserRepository;
import security.example.security.service.UserService;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>());
        return userRepository.save(user);
    }
    @Override
    public Role saveRole(Role role){
        return  roleRepository.save(role);
    }
    @Override
    public void addToUser(String email,String rolename){
        Optional<User> optionalUser=userRepository.findByEmail(email);
        if(!optionalUser.isPresent()){
            throw new IllegalArgumentException("User with email "+email+" does not exits");
        }

        Role role=roleRepository.findByName(rolename);
        if(role == null){
            throw new IllegalArgumentException("Role with name "+rolename+" does not exits");
        }
            User user = optionalUser.get();
            user.getRoles().add(role);//this hashset so we can add


    }



}
