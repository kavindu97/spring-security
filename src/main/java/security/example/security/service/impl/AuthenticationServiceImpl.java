package security.example.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import security.example.security.auth.AuthenticationRequest;
import security.example.security.auth.AuthenticationResponse;
import security.example.security.auth.RegisterRequest;
import security.example.security.model.Role;
import security.example.security.model.User;
import security.example.security.repository.RoleCustomRepo;
import security.example.security.repository.UserRepository;
import security.example.security.service.AuthenticationService;
import security.example.security.service.JwtService;
import security.example.security.service.UserService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleCustomRepo roleCustomRepo;
    private final UserService userService;

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest){
        try{
            if(userRepository.existsById(registerRequest.getEmail().toString())){
                throw new IllegalArgumentException("User with "+registerRequest.getEmail()+"email already exists");
            }

            userService.saveUser(new User(registerRequest.getMobile_number(),registerRequest.getUser_name(),registerRequest.getEmail(),registerRequest.getPassword(),new HashSet<>()));
            userService.addToUser(registerRequest.getEmail(),"ROLE_USER");//default role
            User user=userRepository.findByEmail(registerRequest.getEmail()).orElseThrow();
            return ResponseEntity.ok(user);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }//mekma use krnna admin waltath
    @Override
    public ResponseEntity<?> registerAdmin(RegisterRequest registerRequest){
        try{
            if(userRepository.existsById(registerRequest.getEmail().toString())){
                throw new IllegalArgumentException("User with "+registerRequest.getEmail()+"email already exists");
            }

            userService.saveUser(new User(registerRequest.getMobile_number(),registerRequest.getUser_name(),registerRequest.getEmail(),registerRequest.getPassword(),new HashSet<>()));
            userService.addToUser(registerRequest.getEmail(),"ROLE_ADMIN");//I set this to admin
            User user=userRepository.findByEmail(registerRequest.getEmail()).orElseThrow();
            return ResponseEntity.ok(user);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @Override
    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest){
        try{
            User user=userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(()->new NoSuchElementException("User not found"));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
            List<Role> role=null;
            if(user!=null){
                role=roleCustomRepo.getRole(user);
            }
            Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
            Set<Role> set=new HashSet<>();
            role.stream().forEach(c->{set.add(new Role(c.getName()));
                authorities.add(new SimpleGrantedAuthority(c.getName()));
            });
            user.setRoles(set);
            set.stream().forEach(i->authorities.add(new SimpleGrantedAuthority(i.getName())));
            var jwtAccessToken=jwtService.generateToken(user,authorities);
            var jwtRefershToken=jwtService.generateRefreshToken(user,authorities);
            return ResponseEntity.ok(AuthenticationResponse.builder().access_token(jwtAccessToken).refresh_token(jwtRefershToken).email(user.getEmail()).user_name(user.getUsername()).build());
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (BadCredentialsException e){
            return ResponseEntity.badRequest().body("Invalid Credential");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

}
