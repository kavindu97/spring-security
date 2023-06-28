package security.example.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import security.example.security.auth.dto.AuthenticationRequest;
import security.example.security.auth.dto.AuthenticationResponse;
import security.example.security.auth.dto.RegisterRequest;
import security.example.security.dto.ResponseDto;
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
    private String errorMzg="Internal server error";

    @Override
    public ResponseDto register(RegisterRequest registerRequest) {
        ResponseDto responseDto=new ResponseDto();
        try {
            if (userRepository.existsById(registerRequest.getEmail())) {
                throw new IllegalArgumentException("User with " + registerRequest.getEmail() + "email already exists");
            }

            userService.saveUser(new User(registerRequest.getMobile_number(), registerRequest.getUser_name(), registerRequest.getEmail(), registerRequest.getPassword(), new HashSet<>()));
            userService.addToUser(registerRequest.getEmail(), "ROLE_USER");//default role
            User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow();
            responseDto.setData(user);
            responseDto.setCode(200);
            responseDto.setMzg("Successfully added user");
            return responseDto;
        } catch (IllegalArgumentException e) {
            responseDto.setData((HttpStatus.BAD_REQUEST));
            return  responseDto;
        } catch (Exception e) {
            responseDto.setCode(500);
            responseDto.setMzg(errorMzg);
            return responseDto;
        }
    }//mekma use krnna admin waltath

    @Override
    public ResponseDto registerAdmin(RegisterRequest registerRequest) {
        ResponseDto responseDto=new ResponseDto();
        try {
            if (userRepository.existsById(registerRequest.getEmail())) {
                throw new IllegalArgumentException("User with " + registerRequest.getEmail() + "email already exists");
            }

            userService.saveUser(new User(registerRequest.getMobile_number(), registerRequest.getUser_name(), registerRequest.getEmail(), registerRequest.getPassword(), new HashSet<>()));
            userService.addToUser(registerRequest.getEmail(), "ROLE_ADMIN");//I set this to admin
            User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow();
            responseDto.setData(user);
            responseDto.setCode(200);
            responseDto.setMzg("Successfully added admin");
            return responseDto;
        } catch (IllegalArgumentException e) {
             responseDto.setData((HttpStatus.BAD_REQUEST));
             return  responseDto;
        } catch (Exception e) {
            responseDto.setCode(500);
            responseDto.setMzg(errorMzg);
           return responseDto;
        }
    }

    @Override
    public ResponseDto authenticate(AuthenticationRequest authenticationRequest) {
        ResponseDto responseDto=new ResponseDto();
        try {
            User user = userRepository.findByEmail(authenticationRequest.getEmail())
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
            List<Role> role = roleCustomRepo.getRole(user);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Set<Role> set = new HashSet<>();
            role.stream().forEach(c -> {
                set.add(new Role(c.getName()));
                authorities.add(new SimpleGrantedAuthority(c.getName()));
            });
            user.setRoles(set);
            set.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));
            var jwtAccessToken = jwtService.generateToken(user, authorities);
            var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);

            AuthenticationResponse authenticationResponse=new AuthenticationResponse();
            authenticationResponse.setAccessToken(jwtAccessToken);
            authenticationResponse.setRefreshToken(jwtRefreshToken);
            authenticationResponse.setEmail(user.getEmail());
            authenticationResponse.setUserName(user.getUser_name());
            responseDto.setCode(200);
            responseDto.setData(authenticationResponse);
            responseDto.setMzg("Successfully login user");
            return responseDto;
        } catch (NoSuchElementException e) {
            responseDto.setCode(404);
          responseDto.setMzg("User not found");
            return responseDto;
        } catch (BadCredentialsException e) {
            responseDto.setCode(501);
            responseDto.setMzg("Invalid Password Credentials ");
            return responseDto;
        } catch (Exception e) {
            responseDto.setCode(500);
            responseDto.setData(e);
            return responseDto;
        }
    }

}
