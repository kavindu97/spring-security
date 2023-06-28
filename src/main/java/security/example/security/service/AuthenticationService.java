package security.example.security.service;

import org.springframework.http.ResponseEntity;
import security.example.security.auth.dto.AuthenticationRequest;
import security.example.security.auth.dto.RegisterRequest;

public interface AuthenticationService {
    ResponseEntity<?> register(RegisterRequest registerRequest);
    ResponseEntity<?> registerAdmin(RegisterRequest registerRequest);
    ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest);
}
