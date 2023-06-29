package security.example.security.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import security.example.security.auth.dto.RegisterRequest;
import security.example.security.dto.ResponseDto;
import security.example.security.service.impl.AuthenticationServiceImpl;

@RestController
@RequestMapping("/demo")
@CrossOrigin
public class DemoController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @GetMapping("/admin")
    public String admin(){
        return "This is admin route";
    }
    @GetMapping("/user")
    public String users(){
        return "This is user Route";
    }

    @PostMapping("/admin/new-admin")
    public ResponseDto registerAdmin(@RequestBody RegisterRequest registerRequest){
        return authenticationService.registerAdmin(registerRequest);
    }
}
