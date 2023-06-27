package security.example.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.example.security.auth.RegisterRequest;
import security.example.security.dto.ResponseDto;
import security.example.security.service.impl.AuthenticationServiceImpl;
import security.example.security.service.BookService;

@RestController
@RequestMapping("/demo")
@CrossOrigin
public class DemoController {
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @GetMapping("/admin")
    public ResponseEntity<?> admin(){
        return ResponseEntity.ok("This is Admin Route");
    }
    @GetMapping("/user")
    public ResponseEntity<?> users(){
        return ResponseEntity.ok("This is User Route");
    }
    @GetMapping("/user/all")
    public  ResponseDto allData(){
         return bookService.allData();
    }
    @PostMapping("/admin/new-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authenticationService.registerAdmin(registerRequest));
    }
}
